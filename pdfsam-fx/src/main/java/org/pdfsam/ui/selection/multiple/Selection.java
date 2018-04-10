package org.pdfsam.ui.selection.multiple;

import org.pdfsam.ui.selection.multiple.move.MoveType;

public class Selection {
	private int top = Integer.MAX_VALUE;
    private int bottom = -1;
    
    public Selection() {
    	
    }
    
    public int getTop() {
    	return top;
    }
    
    public int getBottom() {
    	return bottom;
    }
    
    public void setTop(int top) {
    	this.top = top;
    }
    
    public void setBottom(int bottom) {
    	this.bottom = bottom;
    }
    
    /**
     * @return true the selection has been cleared
     */
    public boolean isClearSelection() {
        return top == Integer.MAX_VALUE && bottom == -1;
    }

    /**
     * @return true if its a single row selection event
     */
    public boolean isSingleSelection() {
        return !isClearSelection() && top == bottom;
    }
    
    public boolean canMove(MoveType type, int totalRows) {
        if (isClearSelection()) {
            return false;
        }
        switch (type) {
        case BOTTOM:
            return isSingleSelection() && bottom < totalRows - 1;
        case DOWN:
            return bottom < totalRows - 1;
        case TOP:
            return isSingleSelection() && top > 0;
        default:
            return top > 0;
        }
    }
}
