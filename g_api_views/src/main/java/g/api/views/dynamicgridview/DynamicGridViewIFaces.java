package g.api.views.dynamicgridview;

import android.view.View;

public interface DynamicGridViewIFaces {
    interface OnDropListener {
        void onActionDrop();
    }

    interface OnDragListener {

        void onDragStarted(int position);

        void onDragPositionsChanged(int oldPosition, int newPosition);
    }

    interface OnEditModeChangeListener {
        void onEditModeChanged(boolean inEditMode);
    }

    interface OnSelectedItemBitmapCreationListener {
        void onPreSelectedItemBitmapCreation(View selectedView, int position, long itemId);

        void onPostSelectedItemBitmapCreation(View selectedView, int position, long itemId);
    }

    interface SwitchCellAnimator {
        void animateSwitchCell(final int originalPosition, final int targetPosition);
    }
}
