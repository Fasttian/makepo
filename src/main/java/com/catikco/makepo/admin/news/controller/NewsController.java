package com.catikco.makepo.admin.news.controller;

import com.catikco.makepo.admin.common.DatatablesResponsePageModel;
import com.catikco.makepo.admin.news.model.NewsEditPageModel;
import com.catikco.makepo.admin.news.model.NewsListPageModel;
import com.catikco.makepo.admin.news.model.NewsRequestPageModel;
import com.catikco.makepo.admin.news.service.NewsService;
import com.catikco.makepo.admin.news.service.impl.NewsServiceImpl;
import com.catikco.makepo.model.CallResult;
import com.catikco.makepo.oss.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.catikco.makepo.common.StringUtils.cutContentFileId;
import static com.catikco.makepo.common.StringUtils.parseContentFileId;


/**
 * Create By: Cai Rong fei @Gui Yang
 * Time: 7/1/2018  14:16
 * Description：
 */
@Controller(value = "admin.NewsController")
@RequestMapping
public class NewsController {

    @Autowired
    private NewsService newsService;

    @Autowired
    private FileStorageService fileStorageService;



    /**
     * 后台管理初始页
     * @return
     */
    @RequestMapping("admin-init")
    public String adminInit(HttpServletResponse response){
        return "admin/index";
    }

    /**
     * 新闻管理页面
     * @return
     */
    @RequestMapping("news-list")
    public String newsInit(){
        return "admin/news/news-list";
    }

    /**
     * 加载新闻列表
     * @return
     */
    @RequestMapping("load-news-list")
    @ResponseBody
    public DatatablesResponsePageModel<NewsListPageModel> loadList(NewsRequestPageModel newsRequestPageModel){
        return newsService.getNewsList(newsRequestPageModel);
    }


    /**
     * 初始化新闻编辑/添加页面
     * @return
     */
    @RequestMapping("edit-news")
    public String edit(Integer id, HttpServletRequest request){
        NewsEditPageModel newsEditPageModel = null;
        if(!"".equals(id))
             newsEditPageModel = newsService.loadNews(id);
        if(null != newsEditPageModel)
             request.setAttribute("newsEditPageModel",newsEditPageModel);

        return "admin/news/edit-news";
    }

    /**
     *
     * @param newsEditPageModel 编辑页面model
     * @param response 响应页面请求
     */
    @RequestMapping(value = "/save-news", method = RequestMethod.POST)
    @ResponseBody
    public CallResult<String> saveNews(NewsEditPageModel newsEditPageModel, HttpServletResponse response, HttpServletRequest request){
        CallResult<String> result = new CallResult<>();
        if(1 == newsService.saveNews(newsEditPageModel,response)){
            result.setCode("succeed");
            result.setData("保存成功");
        }else {
            result.setCode("error");
            result.setData("保存失败");
        }

        return result;

    }


    /**
     * 根据id删除新闻
     * @param id
     */
    @RequestMapping("delete-news")
    public void deleteNews(Integer id){

    }

    /**
     * 对富文本编辑器中的图片文件执行保存操作
     * @param multipartFile
     * @param response
     */
    @RequestMapping("/save-file")
    @ResponseBody
    public void saveFiles(@RequestParam("file")MultipartFile multipartFile, HttpServletResponse response){
       fileStorageService.uploads(multipartFile,response,false);
    }




}
