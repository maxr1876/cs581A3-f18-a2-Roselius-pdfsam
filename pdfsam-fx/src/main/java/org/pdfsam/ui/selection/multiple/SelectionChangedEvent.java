/* 
 * This file is part of the PDF Split And Merge source code
 * Created on 26/giu/2013
 * Copyright 2017 by Sober Lemur S.a.s. di Vacondio Andrea (info@pdfsam.org).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as 
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.pdfsam.ui.selection.multiple;

import static org.pdfsam.support.RequireUtils.require;
import static org.pdfsam.support.RequireUtils.requireNotNull;
import static org.pdfsam.support.RequireUtils.requireState;

import java.util.Collection;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.pdfsam.ui.selection.multiple.move.MoveType;

/**
 * Event sent when the selection on the selection table changed
 * 
 * @author Andrea Vacondio
 * 
 */
final class SelectionChangedEvent {

    private Selection selection;
    private int totalRows = 0;

    private SelectionChangedEvent(Collection<? extends Integer> selected) {
        requireNotNull(selected, "Input selection cannot be null");
        selected.forEach(i -> {
            selection.setBottom(Math.max(i, selection.getBottom()));
            selection.setTop(Math.min(i, selection.getTop()));
        });
    }

    private SelectionChangedEvent() {
        // nothing
    }

    /**
     * @return true the selection has been cleared
     */
    public boolean isClearSelection() {
        return selection.isClearSelection();
    }

    /**
     * @return true if its a single row selection event
     */
    public boolean isSingleSelection() {
        return selection.isSingleSelection();
    }

    /**
     * @return the index for the single selection
     * @throws IllegalStateException
     *             if the event is not a single selection
     */
    public int getSingleSelection() {
        requireState(isSingleSelection(), "Single selection expected");
        return selection.getTop();
    }

    public boolean canMove(MoveType type) {
        return selection.canMove(type, totalRows);
    }

    public int getTotalRows() {
        return totalRows;
    }

    /**
     * @return the event where the selection has been cleared
     */
    public static SelectionChangedEvent clearSelectionEvent() {
        return new SelectionChangedEvent();
    }

    /**
     * @param index
     * @return the event where a single selection has been set
     */
    public static SelectionChangedEvent select(Collection<? extends Integer> index) {
        return new SelectionChangedEvent(index);
    }

    /**
     * @param totalNumberOfRows
     * @return the event where the total number of rows available has been set
     */
    public SelectionChangedEvent ofTotalRows(int totalNumberOfRows) {
        require(totalNumberOfRows >= 0, "Cannot select rows if no row is available");
        this.totalRows = totalNumberOfRows;
        return this;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
