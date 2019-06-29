Page({
  data: {
    nickName: ""
  },
  onLoad: function() {
    var that = this
    wx.getUserInfo({
      success: function(res) {
        that.setData({
          nickName: res.userInfo.nickName
        })
        var info = []
        wx.request({
          url: 'http://www.fenggangguo.xyz:8080/DoubanServer/getfork?userName=' + res.userInfo.nickName,
          header: {
            "content-type": "json"
          },
          success: function(option) {
            var cnt = 1;
            var arr = Object.keys(option.data)
            var len = arr.length
            while (true) {
              if (cnt > len) {
                break
              }
              var bookId = option.data["'" + cnt++ + "'"]
              var bookName = option.data["'" + cnt++ + "'"]
              var bookIsbn = option.data["'" + cnt++ + "'"]
              var temp = {
                "bookId": bookId,
                "bookName": bookName,
                "bookIsbn": bookIsbn
              }
              info.push(temp)
            }
            that.setData({
              books: info
            })
          }
        })
      }
    })

  },
  itemBtn: function (event) {
    var bookId = event.currentTarget.dataset.bookId
    wx.navigateTo({
      url: '/pages/detail/detail?bookId=' + bookId,
    })
  }
})