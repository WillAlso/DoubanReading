Page({
  data: {
    bookId: '',
    nickName: ''
  },
  onLoad: function(options) {
    var temp = options.bookId
    this.setData({
      bookId: temp
    })
    var that = this
    wx.request({
      url: 'https://www.fenggangguo.xyz:81/book/' + temp + '?apikey=0df993c66c0c636e29ecbb5344252a4a',
      header:{
        "content-type": "json"
      },
      success: function(res) {
        if(res.statusCode == 200){
          that.setData({
            book: res.data
          })
        }
      }
    })

    wx.request({
      url: 'http://www.fenggangguo.xyz:8080/DoubanServer/getscore?bookId=' + temp,
      header: {
        "content-type": "json"
      },
      success: function(res){
        that.setData({
          process: res.data
        })
      }
    })
  },
  fork: function(){
    var that = this
    wx.getUserInfo({
      success: function (res) {
        that.setData({
          nickName: res.userInfo.nickName
        })
        var userName = that.data.nickName
        var bookId = that.data.book.id
        var bookName = that.data.book.title
        var bookisbn = that.data.book.isbn13
        wx.request({
          url: 'http://www.fenggangguo.xyz:8080/DoubanServer/fork?userName=' + userName + '&bookId=' + bookId + '&bookName=' + bookName + '&bookIsbn=' + bookisbn,
          success: function () {
          }
        })
      }
    })
  },

  sendkindle: function(){
    var that = this
    wx.getUserInfo({
      success: function (res) {
        that.setData({
          nickName: res.userInfo.nickName
        })
        var userName = that.data.nickName
        var bookId = that.data.book.id
        var bookName = that.data.book.title
        var bookisbn = that.data.book.isbn13
        var content = bookName + "\n" + that.data.book.summary
        wx.request({
          url: 'http://www.fenggangguo.xyz:8080/DoubanServer/kindle?userName=' + userName + '&content=' + content + '&filename=' + bookName + '.txt',
          success: function () {
          }
        })
      }
    })
  }
})