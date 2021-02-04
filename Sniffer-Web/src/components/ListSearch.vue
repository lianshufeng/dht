<template>

  <div class="main">
    <!-- 导航 -->
    <div id="Header_head">
      <div>
        <div>
          <div class="SearchForm_form_style_other" id="SearchForm_form">
            <div id="Header_logo">
              <a href="#/">
                <img src="../assets/logo.png">
              </a>
            </div>
            <span id="SearchForm_search_input_wapper">
            <input type="text" name="wd" placeholder="啥都可以搜..." v-model="wd" @keydown="enterKeyDown($event)"/>
          </span>
            <span id="SearchForm_submit_btn_wrapper">
            <button id="SearchForm_submit_btn" v-on:click="search()">搜索</button>
          </span>
          </div>
        </div>
      </div>
    </div>
    <!-- 内容页面 -->
    <div class="Search_container">
      <div class="Search_content_left">

        <div class="Search_result" v-for="item in searchResult" :key="item.id">
          <!--  标题 -->
          <div class="Search_result_title">
            <span v-html="item.title"></span>
            <input type="button" value="下载" style="float: right;" v-on:click="downloadTorrent(item)"/>
          </div>

          <div class="Search_result_information">
            <div class="Information_detail_information">
              <p class="Information_detail_information_Item" v-show="item.fileTypes != null ">
                <span class="Page_Item_title">文档类型：</span>
                <span>{{ item.fileTypes }}</span>
              </p>
              <p class="Information_detail_information_Item">
                <span class="Page_Item_title">文件数量：</span>
                <span>{{item.fileCount}}</span>
              </p>
              <p class="Information_detail_information_Item">
                <span class="Page_Item_title">文件大小：</span>
                <span>{{item.fileTotalSize}}</span>
              </p>
              <p class="Information_detail_information_Item">
                <span class="Page_Item_title">发布时间：</span>
                <span>{{ item.publishTime }}</span>
              </p>
              <p class="Information_detail_information_Item" v-show="item.url != null">
                <span class="Page_Item_title">下载地址：</span>
                <input type="text" v-model="item.url" readonly="readonly" style="width: 85%;border:none;"
                       onmouseover="this.select()"/>
              </p>

              <p class="Information_detail_information_Item" v-show="item.showMoreInfo">
                <span class="Page_Item_title">相关内容：</span>
                <span v-html="item.indexed"></span>
              </p>
              <p style="text-align:center;cursor: pointer;margin: 0 auto;" v-on:click="changeShowInfo(item)">
                <span v-if="item.showMoreInfo">隐藏详情</span>
                <span v-else>显示更多</span>
              </p>
            </div>
          </div>
        </div>

        <!-- 底部翻页-->
        <div id="Page_page_container">

          <div style="text-align: right;font: 10px arial;color: rgb(153, 153, 153)">
            <span>匹配： {{ (contentTotal / 10000).toFixed(2) == 0 ? contentTotal + ' 条':(contentTotal / 10000).toFixed(2) + ' 万条' }}</span>
          </div>

          <div id="Page_page_list">
            <!-- 首页 -->
            <template>
              <span class="Page_up_page" v-show="page.current > 1" v-on:click="skipPage(1)">首页</span>
            </template>
            <!-- 上一页渲染 -->
            <template>
              <span class="Page_up_page" v-show="page.current > 1" v-on:click="skipPage(page.current-1)">上页</span>
            </template>
            <!-- 页码的渲染-->
            <!-- 前面页码 -->
            <template v-for="n in beforeCurrentTotal(page.current) ">
            <span :key="page.current - ( beforeCurrentTotal(page.current) - n  )-1"
                  v-on:click="skipPage( page.current - ( beforeCurrentTotal(page.current) - n  )-1 )">
              {{  page.current - ( beforeCurrentTotal(page.current) - n  )-1  }}
            </span>
            </template>
            <!-- 当前页 -->
            <span class="Page_current_page">
              {{page.current}}
          </span>
            <!-- 后面的页码 -->
            <template v-for="n in afterCurrentTotal(page.current) ">
            <span :key="n + page.current" v-on:click="skipPage(n + page.current)">
              {{n+page.current}}
            </span>
            </template>
            <!-- 下一页的渲染 -->
            <template>
              <span class="Page_next_page" v-show="page.current < page.total"
                    v-on:click="skipPage(page.current+1)">下页</span>
            </template>
            <!-- 末页 -->
            <template>
              <span class="Page_up_page" v-show="page.current < page.total" v-on:click="skipPage(page.total)">尾页</span>
            </template>
          </div>
        </div>
      </div>

      <div class="Search_content_right" ref="Search_content_right">

        <!--智能标签-->
        <div class="RightTopList_top_list">
          <div>
            <p class="RightTopList_content_right_title">推荐榜</p>
          </div>
          <div class="RightTopList_rank_title">
            <span class="RightTopList_float_left">排行</span>
            <span class="RightTopList_float_right">指数</span>
          </div>
          <ul class="RightTopList_rank_list">
            <template v-for="(item,i) in (tagsResult) ">
              <li :key="i">
              <span class="RightTopList_float_left line-limit-length">
                <em class="RightTopList_rank_num"
                    v-bind:class="{ RightTopList_rank_num_3: (i+1)==3, RightTopList_rank_num_2: (i+1)==2 , RightTopList_rank_num_1: (i+1)==1 }">{{i+1}}</em>
                <a href="#" v-on:click="wd='tag:'+item.name;search()">{{ item.name }}</a>
              </span>
                <span class="RightTopList_float_right">
                {{item.count}}
              </span>
              </li>
            </template>
          </ul>
        </div>


        <!--热词榜-->
        <div class="RightTopList_top_list">
          <div>
            <p class="RightTopList_content_right_title">热词榜</p>
          </div>
          <div class="RightTopList_rank_title">
            <span class="RightTopList_float_left">排名</span>
            <span class="RightTopList_float_right">指数</span>
          </div>
          <ul class="RightTopList_rank_list">
            <template v-for="(item,i) in (hotWordsResult) ">
              <li :key="i">
                <span class="RightTopList_float_left line-limit-length">
                  <em class="RightTopList_rank_num"
                      v-bind:class="{ RightTopList_rank_num_3: (i+1)==3, RightTopList_rank_num_2: (i+1)==2 , RightTopList_rank_num_1: (i+1)==1 }">{{i+1}}</em>
                  <a href="#" v-on:click="wd=item.name;search()">{{ item.name }}</a>
                </span>
                <span class="RightTopList_float_right">
                  {{item.count}}
                </span>
              </li>
            </template>
          </ul>
        </div>

      </div>
      <div class="clearfix"></div>
    </div>
    <!-- 底部信息-->
    <div id="Bottom_bottom_wrapper" style="color:#999;">
      <div>
        <p>
          ©2020 金牌找你妹
        </p>
        <p>
          仅为海外华人提供服务，受当地律法保护！
        </p>
      </div>
    </div>
  </div>
</template>

<script>

  import Vue from 'vue'
  import {Tabs, Tab} from 'vue-tabs-component'
  import ElementUtil from '@/components/ElementUtil'
  import ajax from 'axios'

  Vue.component('tabs', Tabs)
  Vue.component('tab', Tab)
  Vue.component('ElementUtil', ElementUtil)
  Vue.component('ajax', ajax)

  export default {
    name: 'ListSearch',
    methods: {
      showRightContent: function (val) {
        // this.$refs.Search_content_right.style.display = val > 900 ? '' : 'none'
        this.$refs.Search_content_right.style.float = val > 900 ? 'right' : 'left'
      },
      beforeCurrentTotal: function (currentPage) {
        return (currentPage < 6 ? currentPage : 6) - 1
      },
      afterCurrentTotal: function (currentPage) {
        if (this.page.total < currentPage) {
          return 0
        }
        let subPagr = (10 - currentPage - 1)
        subPagr = subPagr < 5 ? 5 : subPagr
        return (this.page.total - currentPage) > 5 ? subPagr : (this.page.total - currentPage)
      },
      search: function () {
        this.requestSearch(this.wd, 1)
      },
      enterKeyDown(ev) {
        if (ev.keyCode === 13) {
          this.search()
        }
      },
      downloadTorrent: function (item) {
        let hash = item.url.substring(item.url.lastIndexOf(":") + 1, item.url.length) ;
        let url = ElementUtil.methods.getValueByid('HostUrl') +"dl/"+hash+".torrent"
        window.open(url)
      },
      changeShowInfo: function (item) {
        item.showMoreInfo = !item.showMoreInfo
      },
      openPage: function (item) {
        // 记录hit页面
        let url = ElementUtil.methods.getValueByid('HostUrl') + 'store/hitRecord.json'
        ajax.post(url, 'id=' + item.id).then(function (data) {
        }).catch(function (e) {
          console.error(e)
        })
        // 弹出新页面
        let path = ElementUtil.methods.getValueByid('HostUrl') + 'page/' + item.ref + '.html'
        window.open(path)
      },
      skipPage: function (newPage) {
        this.requestSearch(this.wd, newPage)
      },
      updateHotWordsResult: function () {
        let me = this
        let url = ElementUtil.methods.getValueByid('HostUrl') + 'getHotWords'
        ajax.post(url, '').then(function (data) {
          me.hotWordsResult = data.data.content.content
        }).catch(function (e) {
          console.error(e)
        })
      },
      updateTags: function () {
        let me = this
        let url = ElementUtil.methods.getValueByid('HostUrl') + 'getTags'
        ajax.post(url, '').then(function (data) {
          me.tagsResult = data.data.content
        }).catch(function (e) {
          console.error(e)
        })
      },
      requestSearch: function (keyWord, pageNum) {
        if (this.lastAccessQuery.name === keyWord && this.lastAccessQuery.page === pageNum) {
          return
        }
        this.lastAccessQuery.name = keyWord
        this.lastAccessQuery.page = pageNum
        // console.log('keyWord : ' + keyWord + ' page : ' + pageNum)
        let size = 10
        let me = this
        let url = ElementUtil.methods.getValueByid('HostUrl') + 'search'
        ajax.post(url,
          'keyword=' + keyWord + '&page=' + (pageNum - 1) + '&size=' + size
        ).then(function (data) {
          // 刷新数据
          let content = data.data.content
          // 计算总页数
          me.page.total = content.totalPages
          // 需要请求成功后更改
          me.page.current = pageNum
          // 初始化一些值
          for (let i in content.content) {
            content.content[i]['showMoreInfo'] = false
          }
          // 更新数据到视图
          me.searchResult = content.content
          // 总记录数
          me.contentTotal = content.totalElements
          // 回到顶部
          document.body.scrollTop = document.documentElement.scrollTop = 0
        }).catch(function (e) {
          console.error(e)
        })
      }
    },
    data() {
      return {
        showMoreInfo: {},
        wd: '',
        contentTotal: 0,
        page: {
          total: 1,
          current: 1
        },
        tagsResult: [],
        hotWordsResult: [],
        searchResult: [],
        screenWidth: document.body.clientWidth,
        lastAccessQuery: {
          name: null,
          page: null
        }
      }
    },
    mounted: function () {
      const me = this
      // 进行赋值
      this.wd = typeof (this.$route.params.wd) === 'undefined' ? '' : this.$route.params.wd
      // 首次载入进行搜索
      this.requestSearch(this.wd, 1)
      // 进行监视分辨率发生变化
      window.onresize = function () {
        return (() => {
          window.screenWidth = document.body.clientWidth
          me.screenWidth = window.screenWidth
        })()
      }
      this.showRightContent(me.screenWidth)
      // 更新标签
      this.updateTags()
      // 更新热词表
      this.updateHotWordsResult()
    },
    watch: {
      screenWidth: function (val) {
        this.screenWidth = val
        // 显示右侧栏
        this.showRightContent(val)
      }
    }
  }
</script>

<style scoped>
  .main {
    margin: 0px
  }

  .clearfix {
    height: 0;
    clear: both;
  }

  .Page_Item_title {
    float: left;
    width: 80px;
  }

  #Bottom_bottom_wrapper {
    margin: 0 auto;
    text-align: center;
    font-size: 13px;
    width: 100%;
    position: relative;
    z-index: 10;
  }

  #Page_page_container #Page_page_list span.Page_next_page, #Page_page_container #Page_page_list span.Page_up_page {
    width: auto;
    padding: 0 6px;
  }

  #Page_page_container #Page_page_list span, #Page_page_container #Page_page_list strong {
    font: 14px arial;
    display: inline-block;
    text-align: center;
    line-height: 34px;
    min-width: 34px;
    height: 34px;
    /*height: 34px;*/
    /*width: 34px;*/
    border: 1px solid #e1e2e3;
    margin-right: 4px;
    color: #5a5a58;
    cursor: pointer;
  }

  #Page_page_container {
    margin-top: 60px;
    font-family: arial, Microsoft YaHei, \\5FAE\8F6F\96C5\9ED1, Hiragino Sans GB, tahoma, simsun, \\5B8B\4F53;
  }

  .Search_container {
    padding-top: 20px;
    padding-bottom: 120px;
    position: relative;
    min-height: 350px;
  }

  .Information_detail_information_Item {
    margin: 0 auto;
  }

  .Search_result {
    width: 100%;
    border: 1px solid #efeeee;
    margin-top: 30px;
  }

  .Search_content_right {
    width: 40%;
    float: right;
    vertical-align: top;
    border-left: 1px solid #e1e1e1;
    padding-left: 30px;
    padding-bottom: 20px;
  }

  .Search_content_left {
    width: 45%;
    padding-left: 100px;
    float: left;
  }

  .Search_result_title {
    min-height: 25px;
    background-color: #fafafa;
    padding: 10px 15px;
    color: -webkit-link;
    cursor: pointer;
    word-wrap: break-word;
    display: block;
  }

  #SearchForm_form #SearchForm_submit_btn_wrapper #SearchForm_submit_btn {
    background-color: #f8d305;
    text-align: center;
    font-size: 17px;
    outline: none;
    cursor: pointer;
    border: 1px solid #eac704;
  }

  .SearchForm_form_style_other #SearchForm_submit_btn_wrapper, .SearchForm_form_style_other #SearchForm_submit_btn_wrapper #SearchForm_submit_btn {
    width: 80px;
    height: 48px;
  }

  #Header_head #Header_logo {
    float: left;
  }

  #Header_logo img {
    width: 48px;
    height: 48px;
    border: none;
    text-decoration: none;
    padding: 0px 10px 0px 0px;
  }

  .SearchForm_form_style_other {
    vertical-align: top;
    display: inline-block;
    padding-left: 20px;
  }

  #SearchForm_form #SearchForm_search_input_wapper {
    border: 1px solid #d0d0d0;
    display: inline-block;
    vertical-align: top;
  }

  .SearchForm_form_style_other #SearchForm_search_input_wapper {
    width: 400px;
    height: 46px;
    position: relative;
  }

  #SearchForm_form #SearchForm_search_input_wapper input {
    outline: none;
    border: none;
    font-size: 16px;
    color: #333;
    display: inline-block;
  }

  .SearchForm_form_style_other #SearchForm_search_input_wapper input {
    width: 390px;
    height: 30px;
    margin: 9px 0 0 8px;
    background-color: #fafafa;
  }

  input {
    font-family: Microsoft YaHei, Helvetica Neue Light, Lucida Grande, Calibri, Arial, sans-serif;
  }

  .SearchForm_suggestion_wrapper {
    width: 402px;
    top: 48px;
    left: -1px;
  }

  .SearchForm_suggestion_base {
    position: absolute;
    z-index: 1000;
  }

  #Header_head {
    background-color: #fafafa;
    padding: 20px 10px;
    border-bottom: 1px solid #efeeee;
    position: relative;
  }

  .Information_detail_information {
    line-height: 24px;
    font-size: 13px;
    padding: 10px 15px;
    color: #333;
  }

  .Page_current_page {
    color: #333 !important;
    font-weight: 700 !important;
    height: 36px !important;
    width: 36px !important;
    border: none !important;
    cursor: default !important;
  }

  .hidden {
    display: none !important;
  }

  /*右侧内容*/
  .RightTopList_top_list {
    width: 360px;
    margin-bottom: 30px;
    margin-left: -10px;
  }

  .RightTopList_top_list, li, ul {
    list-style: none;
  }

  .RightTopList_top_list .RightTopList_content_right_title {
    font-size: 14px;
    font-weight: 700;
    margin-bottom: 12px;
  }

  .line-limit-length {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  .RightTopList_float_left {
    float: left;
    width: 65%;
  }

  .RightTopList_float_right {
    float: right;
    width: 35%;
    text-align: right;
  }

  .RightTopList_rank_list {
    font-size: 13px;
  }

  .RightTopList_rank_title {
    height: 32px;
    line-height: 32px;
    background-color: #fafafa;
    color: #666;
    font-weight: 400;
    font-size: 13px;
    border-bottom: 1px solid #f0f0f0;
  }

  .RightTopList_rank_num {
    display: inline-block;
    padding: 1px 0;
    color: #fff;
    width: 14px;
    line-height: 100%;
    font-size: 12px;
    text-align: center;
    margin-right: 5px;
    background-color: #8eb9f5;
  }

  em {
    font-style: normal;
  }

  .RightTopList_rank_list li {
    height: 32px;
    line-height: 32px;
    border-bottom: 1px solid #f0f0f0;
    clear: both;
  }

  .RightTopList_top_list .RightTopList_rank_list a {
    color: blue;
    text-decoration: none;
    outline: none;
  }

  .RightTopList_up_arrow {
    color: #d40707;
  }

  .RightTopList_rank_num_1 {
    background-color: #f54545;
  }

  .RightTopList_rank_num_2 {
    background-color: #ff8547;
  }

  .RightTopList_rank_num_3 {
    background-color: #ffac38;
  }

</style>

<!--tab-->
<!--<style>-->
<!--.tabs-component {-->
<!--margin: 4em 0;-->
<!--}-->
<!--.tabs-component-tabs {-->
<!--border: solid 1px #ddd;-->
<!--border-radius: 6px;-->
<!--margin-bottom: 5px;-->
<!--}-->
<!--@media (min-width: 700px) {-->
<!--.tabs-component-tabs {-->
<!--border: 0;-->
<!--align-items: stretch;-->
<!--display: flex;-->
<!--justify-content: flex-start;-->
<!--margin-bottom: -1px;-->
<!--}-->
<!--}-->
<!--.tabs-component-tab {-->
<!--color: #999;-->
<!--font-size: 14px;-->
<!--font-weight: 600;-->
<!--margin-right: 0;-->
<!--list-style: none;-->
<!--}-->
<!--.tabs-component-tab:not(:last-child) {-->
<!--border-bottom: dotted 1px #ddd;-->
<!--}-->
<!--.tabs-component-tab:hover {-->
<!--color: #666;-->
<!--}-->
<!--.tabs-component-tab.is-active {-->
<!--color: #000;-->
<!--}-->
<!--.tabs-component-tab.is-disabled * {-->
<!--color: #cdcdcd;-->
<!--cursor: not-allowed !important;-->
<!--}-->
<!--@media (min-width: 700px) {-->
<!--.tabs-component-tab {-->
<!--background-color: #fff;-->
<!--border: solid 1px #ddd;-->
<!--border-radius: 3px 3px 0 0;-->
<!--margin-right: .5em;-->
<!--transform: translateY(2px);-->
<!--transition: transform .3s ease;-->
<!--}-->
<!--.tabs-component-tab.is-active {-->
<!--border-bottom: solid 1px #fff;-->
<!--z-index: 2;-->
<!--transform: translateY(0);-->
<!--}-->
<!--}-->
<!--.tabs-component-tab-a {-->
<!--align-items: center;-->
<!--color: inherit;-->
<!--display: flex;-->
<!--padding: .75em 1em;-->
<!--text-decoration: none;-->
<!--}-->
<!--.tabs-component-panels {-->
<!--padding: 4em 0;-->
<!--}-->
<!--@media (min-width: 700px) {-->
<!--.tabs-component-panels {-->
<!--border-top-left-radius: 0;-->
<!--background-color: #fff;-->
<!--border: solid 1px #ddd;-->
<!--border-radius: 0 6px 6px 6px;-->
<!--box-shadow: 0 0 10px rgba(0, 0, 0, .05);-->
<!--padding: 4em 2em;-->
<!--}-->
<!--}-->
<!--</style>-->
