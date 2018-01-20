package cn.peng.pxun.modle.bean;

import java.util.List;

/**
 * Created by tofirst on 2017/10/31.
 */
public class MovieBean {

    public int count;
    public int start;
    public int total;
    public String title;
    public List<SubjectsBean> subjects;

    public static class SubjectsBean {

        public RatingBean rating;
        public String title;
        public int collect_count;
        public String original_title;
        public String subtype;
        public String year;
        public ImagesBean images;
        public String alt;
        public String id;
        public List<String> genres;
        public List<CastsBean> casts;
        public List<DirectorsBean> directors;

        public static class RatingBean {
            /**
             * max : 10
             * average : 9.6
             * stars : 50
             * min : 0
             */
            public int max;
            public double average;
            public String stars;
            public int min;
        }

        public static class ImagesBean {
            /**
             * small : https://img3.doubanio.com/view/movie_poster_cover/ipst/public/p480747492.webp
             * large : https://img3.doubanio.com/view/movie_poster_cover/lpst/public/p480747492.webp
             * medium : https://img3.doubanio.com/view/movie_poster_cover/spst/public/p480747492.webp
             */
            public String small;
            public String large;
            public String medium;
        }

        public static class CastsBean {
            /**
             * alt : https://movie.douban.com/celebrity/1054521/
             * avatars : {"small":"https://img3.doubanio.com/img/celebrity/small/17525.jpg","large":"https://img3.doubanio.com/img/celebrity/large/17525.jpg","medium":"https://img3.doubanio.com/img/celebrity/medium/17525.jpg"}
             * name : 蒂姆·罗宾斯
             * id : 1054521
             */
            public String alt;
            public AvatarsBean avatars;
            public String name;
            public String id;

            public static class AvatarsBean {
                /**
                 * small : https://img3.doubanio.com/img/celebrity/small/17525.jpg
                 * large : https://img3.doubanio.com/img/celebrity/large/17525.jpg
                 * medium : https://img3.doubanio.com/img/celebrity/medium/17525.jpg
                 */
                public String small;
                public String large;
                public String medium;
            }
        }

        public static class DirectorsBean {
            /**
             * alt : https://movie.douban.com/celebrity/1047973/
             * avatars : {"small":"https://img3.doubanio.com/img/celebrity/small/230.jpg","large":"https://img3.doubanio.com/img/celebrity/large/230.jpg","medium":"https://img3.doubanio.com/img/celebrity/medium/230.jpg"}
             * name : 弗兰克·德拉邦特
             * id : 1047973
             */
            public String alt;
            public AvatarsBeanX avatars;
            public String name;
            public String id;

            public static class AvatarsBeanX {
                /**
                 * small : https://img3.doubanio.com/img/celebrity/small/230.jpg
                 * large : https://img3.doubanio.com/img/celebrity/large/230.jpg
                 * medium : https://img3.doubanio.com/img/celebrity/medium/230.jpg
                 */
                public String small;
                public String large;
                public String medium;

            }
        }
    }
}
