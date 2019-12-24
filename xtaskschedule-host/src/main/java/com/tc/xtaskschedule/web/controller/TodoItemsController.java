package com.tc.xtaskschedule.web.controller;

import com.tc.xtaskschedule.repository.TaskRepository;
import com.tc.xtaskschedule.repository.TaskTodoItemsRepository;
import com.tc.xtaskschedule.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by hbxia on 2017/4/21.
 */
@Controller
public class TodoItemsController extends BaseController {

    @Autowired
    private TaskTodoItemsRepository todoItemsRepository;

    @Autowired
    private TaskRepository taskRepository;

    @RequestMapping("/backup")
    public String backup(HttpServletRequest request, Model model){

        todoItemsRepository.backupTaskTodoItems();
        return "done!";
    }
}
