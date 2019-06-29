Page({
  data : {
    userInfo : {
      avatarUrl : "",
      nickName : ""
    }
  },
  onLoad: function (options){
    var that=this
    wx.getUserInfo({
      success: function(res){
        that.setData({
          'userInfo.avatarUrl': res.userInfo.avatarUrl,
          'userInfo.nickName': res.userInfo.nickName
        })
      }
    })
  },
  setVip: function() {
    wx.requestPayment({
      timeStamp: '',
      nonceStr: '',
      package: '',
      signType: '',
      paySign: '',
      success: function(res) {
      }
    })
  },
  bindmail: function(){
    var that = this
    wx.navigateTo({
      url: '/pages/mail/mail?nickName=' + that.data.userInfo.nickName,
    })
  },
  kindle: function() {

  },
  fork: function() {
    wx.navigateTo({
      url: '/pages/favo/favo',
    })
  },
  advide: function() {
    
  }
})