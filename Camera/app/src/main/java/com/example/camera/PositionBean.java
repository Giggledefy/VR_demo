package com.example.camera;



import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by 冯凯 on 2018/1/8.
 * google ocr识别后 获取文字在图片中的坐标
 */

public class PositionBean {


    /**
     * log_id : 5918528449824263284
     * direction : 0
     * words_result_num : 1
     * words_result : [{"vertexes_location":[{"y":26,"x":38},{"y":26,"x":175},{"y":78,"x":175},{"y":78,"x":38}],"chars":[{"char":"中","location":{"width":36,"top":31,"height":46,"left":55}},{"char":"国","location":{"width":37,"top":31,"height":46,"left":98}},{"char":"网","location":{"width":35,"top":31,"height":46,"left":142}}],"min_finegrained_vertexes_location":[{"y":26,"x":38},{"y":26,"x":175},{"y":78,"x":175},{"y":78,"x":38}],"finegrained_vertexes_location":[{"y":26,"x":38},{"y":26,"x":83},{"y":26,"x":127},{"y":26,"x":172},{"y":26,"x":175},{"y":49,"x":175},{"y":71,"x":175},{"y":78,"x":175},{"y":78,"x":131},{"y":78,"x":86},{"y":78,"x":41},{"y":78,"x":38},{"y":56,"x":38},{"y":33,"x":38}],"location":{"width":139,"top":26,"height":54,"left":38},"words":"中国网"}]
     */

    private long log_id;
    private int direction;
    private int words_result_num;
    private List<WordsResultBean> words_result;

    public long getLog_id() {
        return log_id;
    }

    public void setLog_id(long log_id) {
        this.log_id = log_id;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getWords_result_num() {
        return words_result_num;
    }

    public void setWords_result_num(int words_result_num) {
        this.words_result_num = words_result_num;
    }

    public List<WordsResultBean> getWords_result() {
        return words_result;
    }

    public void setWords_result(List<WordsResultBean> words_result) {
        this.words_result = words_result;
    }

    public static class WordsResultBean {
        /**
         * vertexes_location : [{"y":26,"x":38},{"y":26,"x":175},{"y":78,"x":175},{"y":78,"x":38}]
         * chars : [{"char":"中","location":{"width":36,"top":31,"height":46,"left":55}},{"char":"国","location":{"width":37,"top":31,"height":46,"left":98}},{"char":"网","location":{"width":35,"top":31,"height":46,"left":142}}]
         * min_finegrained_vertexes_location : [{"y":26,"x":38},{"y":26,"x":175},{"y":78,"x":175},{"y":78,"x":38}]
         * finegrained_vertexes_location : [{"y":26,"x":38},{"y":26,"x":83},{"y":26,"x":127},{"y":26,"x":172},{"y":26,"x":175},{"y":49,"x":175},{"y":71,"x":175},{"y":78,"x":175},{"y":78,"x":131},{"y":78,"x":86},{"y":78,"x":41},{"y":78,"x":38},{"y":56,"x":38},{"y":33,"x":38}]
         * location : {"width":139,"top":26,"height":54,"left":38}
         * words : 中国网
         */

        private LocationBean location;
        private String words;
        private List<VertexesLocationBean> vertexes_location;
        private List<CharsBean> chars;
        private List<MinFinegrainedVertexesLocationBean> min_finegrained_vertexes_location;
        private List<FinegrainedVertexesLocationBean> finegrained_vertexes_location;

        public LocationBean getLocation() {
            return location;
        }

        public void setLocation(LocationBean location) {
            this.location = location;
        }

        public String getWords() {
            return words;
        }

        public void setWords(String words) {
            this.words = words;
        }

        public List<VertexesLocationBean> getVertexes_location() {
            return vertexes_location;
        }

        public void setVertexes_location(List<VertexesLocationBean> vertexes_location) {
            this.vertexes_location = vertexes_location;
        }

        public List<CharsBean> getChars() {
            return chars;
        }

        public void setChars(List<CharsBean> chars) {
            this.chars = chars;
        }

        public List<MinFinegrainedVertexesLocationBean> getMin_finegrained_vertexes_location() {
            return min_finegrained_vertexes_location;
        }

        public void setMin_finegrained_vertexes_location(List<MinFinegrainedVertexesLocationBean> min_finegrained_vertexes_location) {
            this.min_finegrained_vertexes_location = min_finegrained_vertexes_location;
        }

        public List<FinegrainedVertexesLocationBean> getFinegrained_vertexes_location() {
            return finegrained_vertexes_location;
        }

        public void setFinegrained_vertexes_location(List<FinegrainedVertexesLocationBean> finegrained_vertexes_location) {
            this.finegrained_vertexes_location = finegrained_vertexes_location;
        }

        public static class LocationBean {
            /**
             * width : 139
             * top : 26
             * height : 54
             * left : 38
             */

            private int width;
            private int top;
            private int height;
            private int left;

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public int getTop() {
                return top;
            }

            public void setTop(int top) {
                this.top = top;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public int getLeft() {
                return left;
            }

            public void setLeft(int left) {
                this.left = left;
            }
        }

        public static class VertexesLocationBean {
            /**
             * y : 26
             * x : 38
             */

            private int y;
            private int x;

            public int getY() {
                return y;
            }

            public void setY(int y) {
                this.y = y;
            }

            public int getX() {
                return x;
            }

            public void setX(int x) {
                this.x = x;
            }
        }

        public static class CharsBean {
            /**
             * char : 中
             * location : {"width":36,"top":31,"height":46,"left":55}
             */

            @SerializedName( "char")
            private String charX;
            private LocationBeanX location;

            public String getCharX() {
                return charX;
            }

            public void setCharX(String charX) {
                this.charX = charX;
            }

            public LocationBeanX getLocation() {
                return location;
            }

            public void setLocation(LocationBeanX location) {
                this.location = location;
            }

            public static class LocationBeanX {
                /**
                 * width : 36
                 * top : 31
                 * height : 46
                 * left : 55
                 */

                private int width;
                private int top;
                private int height;
                private int left;

                public int getWidth() {
                    return width;
                }

                public void setWidth(int width) {
                    this.width = width;
                }

                public int getTop() {
                    return top;
                }

                public void setTop(int top) {
                    this.top = top;
                }

                public int getHeight() {
                    return height;
                }

                public void setHeight(int height) {
                    this.height = height;
                }

                public int getLeft() {
                    return left;
                }

                public void setLeft(int left) {
                    this.left = left;
                }
            }
        }

        public static class MinFinegrainedVertexesLocationBean {
            /**
             * y : 26
             * x : 38
             */

            private int y;
            private int x;

            public int getY() {
                return y;
            }

            public void setY(int y) {
                this.y = y;
            }

            public int getX() {
                return x;
            }

            public void setX(int x) {
                this.x = x;
            }
        }

        public static class FinegrainedVertexesLocationBean {
            /**
             * y : 26
             * x : 38
             */

            private int y;
            private int x;

            public int getY() {
                return y;
            }

            public void setY(int y) {
                this.y = y;
            }

            public int getX() {
                return x;
            }

            public void setX(int x) {
                this.x = x;
            }
        }
    }
}
