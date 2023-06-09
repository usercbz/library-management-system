package com.cbz.librarymanagementsystem.utils;

public class RedisConst {

    public static final String LOGIN_TOKEN_KEY_PRE = "login:token:";
    public static final Long LOGIN_TOKEN_TTL = 1L;


    public static final String RETRIEVE_CODE_KEY_PRE = "retrieve:password:code:";
    public static final Long RETRIEVE_CODE_TTL = 3L;

    public static final String QUERY_BOOK_KEY_PRE = "query:books:";
    public static final Long QUERY_BOOK_TTL = 4L;

    public static final String QUERY_COLLECT_BOOK_KEY_PRE = "query:collect:books:";
    public static final Long QUERY_COLLECT_BOOK_TTL = 4L;

    public static final String QUERY_LIST_KEY = "query:books:list";
    public static final Long QUERY_LIST_TTL = 4L;
}
