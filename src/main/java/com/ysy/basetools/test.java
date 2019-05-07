package com.ysy.basetools;

import com.ysy.basetools.export.ExportConvertUtil;
import com.ysy.basetools.util.ExcelUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class test {

    public static void main(String[] args) {

        User u1 = new User(1L,"小明", 1);
        User u2 = new User(2L,"小红", 2);
        User u3 = new User(3L,"小黑", 1);
        List<User> users = Arrays.asList(u1,u2,u3);
        users.stream().forEach(var -> {
            System.out.println(var.toString());
        });

        List<List<String>> data = ExportConvertUtil.getData(User.class, users);
        try {
            byte[] bytes = ExcelUtil.exportExcel("用户信息", data);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

//    window.location.href = CUR_PATH+"/mstRegion/testDownload;
//    @RequestMapping(value = "testDownload", method = RequestMethod.GET)
//    @ResponseBody
//    public void testDownload(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        String fileName = new String("用户信息.xls".getBytes(), Charset.forName("ISO-8859-1"));
//        response.reset();
//        response.setContentType("application/vnd.ms-excel;charset=utf-8");
//        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
//        OutputStream out = response.getOutputStream();
//
//        User u1 = new User(1L,"小明", 1);
//        User u2 = new User(2L,"小红", 2);
//        User u3 = new User(3L,"小黑", 1);
//        List<List<String>> data = ExportConvertUtil.getData(User.class, Arrays.asList(u1,u2,u3));
//        byte[] bytes = null;
//        try {
//            bytes = ExcelUtil.exportExcel("用户信息", data);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        out.write(bytes);
//        out.flush();
//        out.close();
//    }

}
