package dev.highright96.servlet.web.frontcontroller.v5;

import dev.highright96.servlet.web.frontcontroller.ModelView;
import dev.highright96.servlet.web.frontcontroller.MyView;
import dev.highright96.servlet.web.frontcontroller.v3.controller.MemberFormControllerV3;
import dev.highright96.servlet.web.frontcontroller.v3.controller.MemberListControllerV3;
import dev.highright96.servlet.web.frontcontroller.v3.controller.MemberSaveControllerV3;
import dev.highright96.servlet.web.frontcontroller.v5.adapter.ControllerV3HandlerAdapter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "frontControllerServletV5", urlPatterns = "/front-controller/v5/*")
public class FrontControllerServletV5 extends HttpServlet {

    private final Map<String, Object> handlerMappingMap = new HashMap<>();
    private final List<MyHandlerAdapter> handlerAdapters = new ArrayList<>();

    public FrontControllerServletV5() {
        initHandlerMappingMap();
        initHandlerAdapters();
    }

    private void initHandlerAdapters() {
        handlerAdapters.add(new ControllerV3HandlerAdapter());
    }

    private void initHandlerMappingMap() {
        handlerMappingMap.put("/front-controller/v5/v3/members/new-form", new MemberFormControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members/save", new MemberSaveControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members", new MemberListControllerV3());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //핸들러 조회
        Object handler = getHandler(request);

        if (handler == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }

        //핸들러를 처리할 수 있는 핸들러 어탭터 조회
        MyHandlerAdapter myHandlerAdapter = getHandlerAdapter(handler);

        //찾은 핸들러 어탭터 호출(핸들러를 넘겨줌)
        ModelView mv = myHandlerAdapter.handle(request, response, handler);
        String viewName = mv.getViewName();

        MyView view = viewResolver(viewName);
        view.render(mv.getModel(), request, response);
    }

    private MyHandlerAdapter getHandlerAdapter(Object handler) {
        for (MyHandlerAdapter adapter : handlerAdapters) {
            if (adapter.supports(handler)) return adapter;
        }
        throw new IllegalStateException("handler adapter를 찾을 수 없습니다. handler" + handler);
    }

    private Object getHandler(HttpServletRequest request) {
        String requestUrl = request.getRequestURI();
        return handlerMappingMap.get(requestUrl);
    }

    private MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }

}