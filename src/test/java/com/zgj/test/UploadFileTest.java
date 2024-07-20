package com.zgj.test;

import org.junit.jupiter.api.Test;

public class UploadFileTest {

    @Test
    public void test(){

        String filename = "ere.r.e.we.jpg";
        String suffix = filename.substring(filename.lastIndexOf("."));
        System.out.println(suffix);

    }
}
