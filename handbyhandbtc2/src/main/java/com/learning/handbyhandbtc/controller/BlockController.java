package com.learning.handbyhandbtc.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.handbyhandbtc.bean.Block;
import com.learning.handbyhandbtc.bean.Notebook;
import com.learning.handbyhandbtc.bean.Transaction;
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
    public String addNote(Transaction transaction) {
        try {
            if (transaction.verify()) {
                ObjectMapper objectMapper = new ObjectMapper();
                String transactionString = objectMapper.writeValueAsString(transaction);
                notebook.addNote(transactionString);
                return "添加记录成功";
            } else {
                throw new RuntimeException("交易数据校验失败");
            }

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
