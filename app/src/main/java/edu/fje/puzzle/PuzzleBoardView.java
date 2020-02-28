package edu.fje.puzzle;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;

class PuzzleBoardView extends View {
    Context context;
    private MediaPlayer mp;
    public static final int NUM_SHUFFLE_STEPS = 40;
    private Activity activity;
    private PuzzleBoard puzzleBoard;
    private ArrayList<PuzzleBoard> animation;
    private Random random = new Random();
    Comparator<PuzzleBoard> comparator = new PuzzleBoardComparator();
    PriorityQueue<PuzzleBoard> queue = new PriorityQueue<>(9999, comparator);

    public PuzzleBoardView(Context context) {
        super(context);
        activity = (Activity) context;
        animation = null;
    }

    public void initialize(Bitmap imageBitmap) {
        int width = getWidth();
        puzzleBoard = new PuzzleBoard(imageBitmap, width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (puzzleBoard != null) {
            if (animation != null && animation.size() > 0) {
                puzzleBoard = animation.remove(0);
                puzzleBoard.draw(canvas);
                if (animation.size() == 0) {
                    animation = null;
                    puzzleBoard.reset();
                    Toast toast = Toast.makeText(activity, "Solved! ", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    this.postInvalidateDelayed(500);
                }
            } else {
                puzzleBoard.draw(canvas);
            }
        }
    }

    public void shuffle() {
        if (animation == null && puzzleBoard != null) {
            // Do something. Then:
            ArrayList<PuzzleBoard> boards;
            for (int i = 0; i <= NUM_SHUFFLE_STEPS; i++) {
                boards = puzzleBoard.neighbours();
                puzzleBoard = boards.get(random.nextInt(boards.size()));
            }
            puzzleBoard.reset();
            invalidate();
            queue.clear();
        }
    }

    public void so(){
        mp = MediaPlayer.create(context, R.raw.b);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (animation == null && puzzleBoard != null) {
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (puzzleBoard.click(event.getX(), event.getY())) {
                        invalidate();
                        if (puzzleBoard.resolved()) {
                            Toast toast = Toast.makeText(activity, "Congratulations!", Toast.LENGTH_LONG);
                            toast.show();
                        }
                        return true;
                    }
            }
        }
        return super.onTouchEvent(event);
    }


    public void solve() {
        puzzleBoard.steps = 0;
        puzzleBoard.previousBoard = null;
        queue.add(puzzleBoard);
        PuzzleBoard prev = null;
        ArrayList<PuzzleBoard> solution = new ArrayList<>();
        while (!queue.isEmpty()) {
            PuzzleBoard lowest = queue.poll();
            if (lowest.priority() - lowest.steps != 0) {
                for (PuzzleBoard toAdd : lowest.neighbours()) {
                    if (!toAdd.equals(prev)) {
                        queue.add(toAdd);
                    }
                }
                prev = lowest;
            }
            else {
                solution.add(lowest);
                while (lowest != null) {
                    if (lowest.getPreviousBoard() == null) {
                        break;
                    }
                    solution.add(lowest.getPreviousBoard());
                    lowest = lowest.getPreviousBoard();
                }
                Collections.reverse(solution);
                animation = solution;
                invalidate();
                break;
            }
        }
    }
}


class PuzzleBoardComparator implements Comparator<PuzzleBoard> {
    @Override
    public int compare(PuzzleBoard first, PuzzleBoard second) {
        if (first.priority() == second.priority()) {
            return 0;
        }
        else if (first.priority() < second.priority()) {
            return -1;
        }
        else {
            return 1;
        }
    }
}