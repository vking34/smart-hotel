package com.hust.smarthotel.generic.constant;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class MsgCode {
    public static final Map<Integer, String> USER_CREATION_ERRORS = ImmutableMap.of(
            101, "Username exists",
            102, ""
    );
}
