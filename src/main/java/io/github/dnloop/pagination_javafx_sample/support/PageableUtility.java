/*
 * MIT License
 *
 * Copyright (c) 2024 Emi Rodriguez
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.dnloop.pagination_javafx_sample.support;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Utility wrapper class used to create a Pageable property used in a repository.
 * The values returned are of page request. Each time a page is needed, this class
 * provides them with required arguments
 */
public class PageableUtility {

    private static final int SIZE = 50;

    private PageableUtility() { }

    /**
     * Get page with initial page value, default {@link #SIZE}  ordered
     * by most recent creation date.
     */
    public static Pageable of() {
        return PageRequest.of(0, SIZE, getSort("createdAt", false));
    }

    /**
     * Get page number with default {@link #SIZE} and most
     * recent creation date.
     */
    public static Pageable of(int page) {
        return PageRequest.of(page, SIZE, getSort("createdAt", false));
    }

    /**
     * Get page with page initial value,
     * default {@link #SIZE} value, using
     * a sort condition with ascending order.
     *
     * @param condition column name
     */
    public static Pageable of(String condition) {
        return PageRequest.of(0, SIZE, getSort(condition, true));
    }

    /**
     * Get page with page initial value,
     * default {@link #SIZE} value. The order
     * direction is defined by ascending (true)
     * or descending (false).
     *
     * @param condition column name
     * @param ascending order direction
     */
    public static Pageable of(String condition, boolean ascending) {
        return PageRequest.of(0, SIZE, getSort(condition, ascending));
    }

    /**
     * Get page with default {@link #SIZE} value using
     * a sort condition with ascending order.
     *
     * @param condition column name
     * @param page      required page number
     */
    public static Pageable of(int page, String condition) {
        return PageRequest.of(page, SIZE, getSort(condition, true));
    }

    /**
     * Get page with default page size, ascension order
     * and custom condition.
     * ascending order.
     *
     * @param condition column name
     * @param page      page number
     * @param ascending order direction
     */
    public static Pageable of(int page, String condition, boolean ascending) {
        return PageRequest.of(page, SIZE, getSort(condition, ascending));
    }

    /**
     * Get page with default ascending order, custom page size and condition.
     *
     * @param condition column name
     * @param page      page number
     * @param size      page size
     */
    public static Pageable of(int page, int size, String condition) {
        return PageRequest.of(page, size, getSort(condition, true));
    }

    /**
     * Get page with fully custom values.
     * The order direction is defined by ascending (true)
     * or descending (false).
     *
     * @param condition column name
     * @param page      page number
     * @param size      page size
     * @param ascending order direction
     */
    public static Pageable of(int page, int size, String condition, boolean ascending) {
        return PageRequest.of(page, size, getSort(condition, ascending));
    }

    /**
     * Get page with fully custom sort and default page size.
     *
     * @param page page number
     * @param sort page size
     */
    public static Pageable of(int page, Sort sort) {
        return PageRequest.of(page, SIZE, sort);
    }

    /**
     * Get page with fully custom sort and size.
     *
     * @param page page number
     * @param size page size
     * @param sort page size
     */
    public static Pageable of(int page, int size, Sort sort) {
        return PageRequest.of(page, size, sort);
    }

    /**
     * Some fields are used as property such as 'createdAt', 'modifiedAt', 'deletedAt'. When
     * used in Sort.by(Sort.Order.desc(condition).ignoreCase()) it throws an InvalidDataAccessApiUsageException
     * due to being of type String.
     * <p>
     * On the other hand they work fine with Sort.by("createdAt").
     * TODO this is getting out of hands, refactor this to include different date fields
     */
    private static Sort getSort(String condition, boolean ascending) {
        if (ascending) {
            switch (condition) {
                case "createdAt", "modifiedAt", "deletedAt", "generationDate" -> {
                    return Sort.by(condition).ascending();
                }
                default -> {
                    return Sort.by(Sort.Order.asc(condition).ignoreCase());
                }
            }
        } else {
            switch (condition) {
                case "createdAt", "modifiedAt", "deletedAt", "generationDate" -> {
                    return Sort.by(condition).descending();
                }
                default -> {
                    return Sort.by(Sort.Order.desc(condition).ignoreCase());
                }
            }
        }
    }

}
