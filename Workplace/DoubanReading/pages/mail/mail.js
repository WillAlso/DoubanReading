Page({
  data: {
    content: "",
    code: "",
    mcode: "",
    tcode: ""
  },
  inputtext: function(res){
    var text = res.detail.value
    this.setData({
      content: text
    })
  },
  sendBtn: function() {
    var that = this
    var con = that.data.content
    wx.request({
      url: 'http://www.fenggangguo.xyz:8080/DoubanServer/Sendmail?email=' + con,
      header: {
        "content-type": "json"
      },
      success: function(res){
        that.setData({
          tcode: res.data.code
        })
      }
    })
  },
  bindBtn: function() {
    var t1 = this.data.code
    var t2 = this.data.tcode
    var that = this
    var name = that.data.nickName
    if(t1 == t2){
      wx.showToast({
        title: '绑定成功',
      })
      wx.request({
        url: 'http://47.103.3.188:8080/DoubanServer/adduser?username=' + name + '&email1=' + that.data.content + '&email2=""',
      })
      var pages = getCurrentPages()
      var before = pages[pages.length - 2]
      wx.navigateBack({
        success: function () {
          before.onLoad()
        }
      })
    }else{
      wx.showToast({
        title: '绑定失败',
      })
    }
  },
  inputcode: function(res){
    var text = res.detail.value
    this.setData({
      code: text
    })
  },
  onLoad: function(res){
    var name = res.nickName
    this.setData({
      'nickName': name
    })
  }
})