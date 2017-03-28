package com.shutup.todo.model.response;

import com.shutup.todo.model.persist.Todo;

import java.util.Date;
import java.util.List;

/**
 * Created by shutup on 2017/3/5.
 */
public class RemoteTodoResponse {
    /**
     * content : [{"id":3,"createdAt":1488680925597,"updatedAt":1488680925597,"todo":"string","delete":false,"finish":false}]
     * last : true
     * totalPages : 1
     * totalElements : 1
     * size : 10
     * number : 0
     * sort : null
     * first : true
     * numberOfElements : 1
     */

    private boolean last;
    private int totalPages;
    private int totalElements;
    private int size;
    private int number;
//    private Object sort;
    private boolean first;
    private int numberOfElements;
    private List<ContentEntity> content;

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

//    public Object getSort() {
//        return sort;
//    }
//
//    public void setSort(Object sort) {
//        this.sort = sort;
//    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public List<ContentEntity> getContent() {
        return content;
    }

    public void setContent(List<ContentEntity> content) {
        this.content = content;
    }

    public static class ContentEntity {
        /**
         * id : 3
         * createdAt : 1488680925597
         * updatedAt : 1488680925597
         * todo : string
         * delete : false
         * finish : false
         */

        private long id;
        private long createdAt;
        private long updatedAt;
        private String todo;
        private boolean delete;
        private boolean finish;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public long getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(long createdAt) {
            this.createdAt = createdAt;
        }

        public long getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(long updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getTodo() {
            return todo;
        }

        public void setTodo(String todo) {
            this.todo = todo;
        }

        public boolean isDelete() {
            return delete;
        }

        public void setDelete(boolean delete) {
            this.delete = delete;
        }

        public boolean isFinish() {
            return finish;
        }

        public void setFinish(boolean finish) {
            this.finish = finish;
        }

        public Todo createTodo() {
            Todo todo = new Todo(this.todo);
            todo.setSid(this.id);
            todo.setSynced(true);
            todo.setCreated(true);
            todo.setUpdatedAt(new Date(this.updatedAt));
            todo.setCreatedAt(new Date(this.createdAt));
            todo.setDelete(this.delete);
            todo.setFinish(this.finish);
            return todo;
        }
    }
}
