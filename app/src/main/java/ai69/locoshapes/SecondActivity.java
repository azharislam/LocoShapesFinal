package ai69.locoshapes;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class SecondActivity extends AppCompatActivity {

    int n;
    ImageView shape1, shape2, shape3, shape4, guessShape;
    ImageButton exit;
    private android.widget.RelativeLayout.LayoutParams layoutParams;
    Random rand = new Random();
    ImageView[] shapes = new ImageView[4];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        //declare each imageview
        shape1 = (ImageView) findViewById(R.id.shape1);
        shape2 = (ImageView) findViewById(R.id.shape2);
        shape3 = (ImageView) findViewById(R.id.shape3);
        shape4 = (ImageView) findViewById(R.id.shape4);
        guessShape = (ImageView) findViewById(R.id.guessShape);

        //add each imageView to the shapes[] array
        shapes[0] = shape1;
        shapes[1] = shape2;
        shapes[2] = shape3;
        shapes[3] = shape4;

        //store all the shapes in an array
        int[] images = new int[]{R.drawable.img_0, R.drawable.img_1, R.drawable.img_2, R.drawable.img_3, R.drawable.img_4,
                R.drawable.img_5, R.drawable.img_6, R.drawable.img_7, R.drawable.img_8, R.drawable.img_9, R.drawable.img_10,
                R.drawable.img_11, R.drawable.img_12, R.drawable.img_13, R.drawable.img_14, R.drawable.img_15, R.drawable.img_16,
                R.drawable.img_17};

        //store all the guessShapes in an array
        int[] outlines = new int[]{R.drawable.outline_0, R.drawable.outline_1, R.drawable.outline_2,
                R.drawable.outline_3, R.drawable.outline_4, R.drawable.outline_5, R.drawable.outline_6,
                R.drawable.outline_7, R.drawable.outline_8, R.drawable.outline_9, R.drawable.outline_10,
                R.drawable.outline_11, R.drawable.outline_12, R.drawable.outline_13, R.drawable.outline_14,
                R.drawable.outline_15, R.drawable.outline_16, R.drawable.outline_17};

        //generate 4 random images from the array's and ensure that they don't match each other
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < 18; i++) {
            list.add(new Integer(i));
        }
        Collections.shuffle(list);
        int whichImg = (int) Math.round((Math.random() * 4));
        int img1 = list.get(0);
        int img2 = list.get(1);
        int img3 = list.get(2);
        int img4 = list.get(3);

        if (whichImg == 1) {
            whichImg = img1;
        } else if (whichImg == 2) {
            whichImg = img2;
        } else if (whichImg == 3) {
            whichImg = img3;
        } else {
            whichImg = img4;
        }

        int outlineID = outlines[whichImg];

        //set the shape in each imageview
        guessShape.setBackgroundResource(outlineID);
        shape1.setBackgroundResource(images[img1]);
        shape2.setBackgroundResource(images[img2]);
        shape3.setBackgroundResource(images[img3]);
        shape4.setBackgroundResource(images[img4]);

        //ensures that 1/4 shape has the guess shape correspondence
        final Object currentBackground = guessShape.getBackground().getConstantState();

        //for loop to have the guess shape and 1/4 shapes to match
        for (int i = 0; i < 18; i++) {
            if (currentBackground.equals(getResourceID("outline_" + i, "drawable", getApplicationContext()))) {
                int random = new Random().nextInt(shapes.length);
                shapes[random].setBackgroundResource(getResourceID("img_" + i, "drawable", getApplicationContext()));
            }

            //set tags for each view
            guessShape.setTag("gShape");
            shape1.setTag("S_1");
            shape2.setTag("S_2");
            shape3.setTag("S_3");
            shape4.setTag("S_4");

            shape1.setOnTouchListener(new MyTouchListener());
            shape2.setOnTouchListener(new MyTouchListener());
            shape3.setOnTouchListener(new MyTouchListener());
            shape4.setOnTouchListener(new MyTouchListener());
            guessShape.setOnDragListener(new MyDragListener());
        }
    }


    //method to get the ID of an image in drawable folder
    protected final static int getResourceID(final String resName, final String resType, final Context ctx)
    {
        final int ResourceID =
                ctx.getResources().getIdentifier(resName, resType,
                        ctx.getApplicationInfo().packageName);
        if (ResourceID == 0)
        {
            throw new IllegalArgumentException
                    (
                            "No resource string found with name " + resName
                    );
        }
        else
        {
            return ResourceID;
        }
    }
    private final class MyTouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                        view);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    view.startDragAndDrop(data, shadowBuilder, view, 0);
                } else {
                    view.startDrag(data, shadowBuilder, view, 0);
                }
                view.setVisibility(View.INVISIBLE);
                return true;
            } else {
                return false;
            }
        }
    }

    class MyDragListener implements View.OnDragListener {
        final Object currentBackground = guessShape.getBackground().getConstantState();

        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (action) {
                case DragEvent.ACTION_DRAG_STARTED:
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    View view = (View) event.getLocalState();
                    for(int i = 0; i < 18; i++) {
                        if (view.getId() == getResourceID("outline_" + i, "drawable", getApplicationContext())
                                && v.getId() == getResourceID("image_" + i, "drawable", getApplicationContext())) {
                            ViewGroup from = (ViewGroup) view.getParent();
                            from.removeView(view);
                            v.setBackgroundResource(getResourceID("image_" + i, "drawable", getApplicationContext()));
                        }
                    }
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    for(int i = 0; i < 18; i++){
                            v.setBackgroundResource(getResourceID("outline_" + i, "drawable", getApplicationContext()));
                        }
                    break;
                case DragEvent.ACTION_DROP:
                    // Dropped, reassign View to ViewGroup
                       view = (View) event.getLocalState();
                    for(int i = 0; i < 18; i++) {
                        if (view.getId() == getResourceID("outline_" + i, "drawable", getApplicationContext())
                                && v.getId() == getResourceID("image_" + i, "drawable", getApplicationContext())) {
                            ViewGroup from = (ViewGroup) view.getParent();
                            from.removeView(view);
                            v.setBackgroundResource(getResourceID("image_" + i, "drawable", getApplicationContext()));
                        }
                    }
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                default:
                    break;
            }
            return true;
        }
    }
}


