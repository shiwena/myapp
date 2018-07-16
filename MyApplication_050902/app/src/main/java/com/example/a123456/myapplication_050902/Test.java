package com.example.a123456.myapplication_050902;

import static org.junit.Assert.assertEquals;

public class Test {


    @org.junit.Test
    public void testlogin(){
	// test login
         assertEquals(false, new LoginActivity().login("1", "11"));
    }
    @org.junit.Test
    public void register(){
        // test register"已被注册"

        assertEquals(false, new RegisterActivity().register("1","1"));

    }

    //
    @org.junit.Test
    public void register1(){
        // test register"账号为空，密码6位"

        assertEquals(false, new RegisterActivity().register("","123456"));

    }
    @org.junit.Test
    public void register2(){
        // test register"账号不为空，密码少于6位"

        assertEquals(false, new RegisterActivity().register("456","12345"));

    }
    @org.junit.Test
    public void register3(){
        // test register"注册成功"

        assertEquals(true, new RegisterActivity().register("a123","123456"));



    }






}
