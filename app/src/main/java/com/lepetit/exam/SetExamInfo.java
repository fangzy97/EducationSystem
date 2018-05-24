package com.lepetit.exam;

public class SetExamInfo {
    private final String course;
    private final String time;
    private final String classroom;
    private final String seat;

    private SetExamInfo(Builder builder) {
        course = builder.mCourse;
        time = builder.mTime;
        classroom = builder.mClassroom;
        seat = builder.mSeat;
    }

    public static class Builder {
        private String mCourse;
        private String mTime;
        private String mClassroom;
        private String mSeat;

        public Builder course(String value) {
            this.mCourse = value;
            return this;
        }

        public Builder time(String value) {
            this.mTime = value;
            return this;
        }

        public Builder classroom(String value) {
            this.mClassroom = value;
            return this;
        }

        public Builder seat(String value) {
            this.mSeat = value;
            return this;
        }

        public SetExamInfo build() {
            return new SetExamInfo(this);
        }
    }
}
