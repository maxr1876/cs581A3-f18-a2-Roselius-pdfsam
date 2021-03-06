/* 
 * This file is part of the PDF Split And Merge source code
 * Created on 22 giu 2016
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
package org.pdfsam.support.params;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.sejda.conversion.AdapterUtils.splitAndTrim;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.pdfsam.i18n.DefaultI18nContext;
import org.sejda.common.collection.NullSafeSet;
import org.sejda.conversion.exception.ConversionException;
import org.sejda.model.pdf.page.PageRange;

/**
 * @author Andrea Vacondio
 *
 */
public final class ConversionUtils {

    private ConversionUtils() {
        // hide
    }

    /**
     * @return the {@link PageRange} set for the given string, an empty set otherwise.
     */
    public static Set<PageRange> toPageRangeSet(String selection) throws ConversionException {
        if (isNotBlank(selection)) {
            Set<PageRange> pageRangeSet = new NullSafeSet<>();
            String[] tokens = splitAndTrim(selection, ",");// split input list on comma
            for (String current : tokens) {
                PageRange range = toPageRange(current);
                if (range.getEnd() < range.getStart()) {
                    throw new ConversionException(
                            DefaultI18nContext.getInstance().i18n("Invalid range: {0}.", range.toString()));
                }
                pageRangeSet.add(range);
            }
            //Now that initial set of page ranges constructed, check to ensure no intersections exist
            
            return removeIntersections(pageRangeSet);
        }
        return Collections.emptySet();
    }

    private static Set<PageRange> removeIntersections(Set<PageRange> ranges){
    	SortedSet<Integer> allPages = new TreeSet<>();
    	Set<PageRange> newRanges = new NullSafeSet<>();
    	for (PageRange range : ranges) {
    		for (int i = range.getStart(); i <= range.getEnd(); i++){
    			allPages.add(i);
    		}
    	}
    	//Now have a set that includes every page number specified by user
    	//create all possible ranges from this set:
    	Object[] sortedArr = allPages.toArray();
    	Integer[] sorted = new Integer[sortedArr.length];
    	for (int i =0; i < sortedArr.length; i++)
            sorted[i] = (Integer)sortedArr[i];
    	List<List<Integer>> result = new ArrayList<List<Integer>>();
        List<Integer> curr = null;
        for (int i = 0; i < sorted.length; i++) {
            if(i == 0 || (sorted[i] != sorted[i-1]+1)) { 
                //if the current element is the first element or doesn't satisfy the condition
                curr = new ArrayList<Integer>(); //create a new list and set it to curr
                result.add(curr); //add the newly created list to the result list
            }
        curr.add(sorted[i]); //add current element to the curr list
        }
    	for (List<Integer> subList : result) {
    		newRanges.add(new PageRange(subList.get(0), subList.get(subList.size()-1)));
    	}
   
    	return newRanges;
    }
    
    
    private static PageRange toPageRange(String value) throws ConversionException {
        String[] limits = splitAndTrim(value, "-");
        if (limits.length > 2) {
            throw new ConversionException(DefaultI18nContext.getInstance().i18n(
                    "Ambiguous page range definition: {0}. Use following formats: [n] or [n1-n2] or [-n] or [n-]",
                    value));
        }
        if (limits.length == 1) {
            int limitNumber = parsePageNumber(limits[0]);
            if (value.endsWith("-")) {
                return new PageRange(limitNumber);
            }
            if (value.startsWith("-")) {
                return new PageRange(1, limitNumber);
            }
            return new PageRange(limitNumber, limitNumber);
        }
        return new PageRange(parsePageNumber(limits[0]), parsePageNumber(limits[1]));
    }

    private static int parsePageNumber(String value) throws ConversionException {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException nfe) {
            throw new ConversionException(DefaultI18nContext.getInstance().i18n("Invalid number: {0}.", value));
        }
    }
}
