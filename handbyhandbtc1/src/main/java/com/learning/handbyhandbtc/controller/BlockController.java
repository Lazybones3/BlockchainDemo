package com.learning.handbyhandbtc.controller;


import com.learning.handbyhandbtc.bean.Block;
import com.learning.handbyhandbtc.bean.Notebook;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class BlockController {

    private Notebook notebook = new Notebook();


    @RequestMapping(value = "/addGenesis", method = RequestMethod.POST)
    public String addGenesis(String genesis) {
        try {
            notebook.addGenesis(genesis);
            return "添加封面成功";
        } catch (Exception e) {
            return "添加封面失败" + e.getMessage();
        }
    }

    @RequestMapping(value = "/addNote", method = RequestMethod.POST)
    public String addNote(String note) {
        try {
            notebook.addNote(note);
            return "添加记录成功";
        } catch (Exception e) {
            return "添加记录失败" + e.getMessage();
        }
    }

    @RequestMapping(value = "/showlist", method = RequestMethod.GET)
    public ArrayList<Block> showlist() {
        return notebook.showlist();
    }

    //校验数据
    @RequestMapping(value = "/check", method = RequestMethod.GET)
    public String check() {
        String check = notebook.check();
        if (StringUtils.isEmpty(check)) {
            return "数据安全";
        }
        return check;
    }
}
