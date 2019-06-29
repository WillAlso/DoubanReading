Page({
  data: {
    content: ""
  },
  searchBtn: function() {
    var con = this.data.content
    var book = []
    var that = this
    wx.request({
      url: 'https://www.fenggangguo.xyz:81/book/search?q=' + con + '&apikey=0df993c66c0c636e29ecbb5344252a4a',
      header: {
        "content-type": "json"
      },
      success: function(res){
        that.setData({
          books: res.data.books
        })
      }
    })
  },
  floatBtn: function() {
    var that = this
    wx.scanCode({
      onlyFromCamera: true,
      success (res){
        var isbn = res.result
        if (res.errMsg == 'scanCode:ok'){
          wx.request({
            url: 'https://www.fenggangguo.xyz:81/book/search?q=' + isbn + '&apikey=0df993c66c0c636e29ecbb5344252a4a',
            header: {
              "content-type": "json"
            },
            success: function(res){
              that.setData({
                books: res.data.books
              })
            }
          })
        }
      }
    })
  },
  onLoad: function(options){
    var input = options.input
    this.setData({
      content: input
    })
  },
  searchitem: function(event){
    var bookId = event.currentTarget.dataset.item
    wx.navigateTo({
      url: '/pages/detail/detail?bookId=' + bookId,
    })
  },
  serachtext: function(res) {
    var con = res.detail.value
    this.setData({
      content: con
    })
  }
})