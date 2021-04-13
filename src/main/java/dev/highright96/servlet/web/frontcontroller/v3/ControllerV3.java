package dev.highright96.servlet.web.frontcontroller.v3;

import dev.highright96.servlet.web.frontcontroller.ModelView;

import java.util.Map;

public interface ControllerV3 {

    ModelView process(Map<String, String> paramMap);

}
