Page({
  data: {
    booknum: 0,
  },
  onLoad: function(options) {
    var barnum = options.barNum
    if(barnum == '1'){
      this.setData({
        booknum: 25,
        tomcaturl: "Newbook"
      })
    }else if(barnum == '2'){
      this.setData({
        booknum: 25,
        tomcaturl: "Remmend"
      })
    }else if(barnum == '3'){
      this.setData({
        booknum: 10,
        tomcaturl: "Popular"
      })
    }else{
      this.setData({
        booknum: 25,
        tomcaturl: Top
      })
    }
    var that = this
    var bookImage = []
    var temp = {
      "title": "",
      "image": "",
      "author": "",
      "isbn": ""
    }
    
    wx.request({
      url: 'http://www.fenggangguo.xyz:8080/DoubanServer/' + that.data.tomcaturl,
      header: {
        "content-type": "json"
      },
      success: function(res) {
        that.setData({
          books: res.data
        })
        if (that.data.booknum > 0) {
          var end = that.data.booknum
          for (var i = 1; i < end + 1; i++) {
            var bookId = that.data.books[i]
            var myurl =
              wx.request({
                url: 'https://www.fenggangguo.xyz:81/book/' + bookId + '?apikey=0df993c66c0c636e29ecbb5344252a4a',
                header: {
                  "content-type": "json"
                },
                success: function(res) {
                  var ttitle = res.data.title
                  var timage = res.data.image
                  var tauthor = res.data.author
                  var tisbn = res.data.isbn13
                  var tbookId = res.data.id
                  var temp = {
                    "title": ttitle,
                    "image": timage,
                    "author": tauthor,
                    "isbn": tisbn,
                    "bookId": tbookId
                  }
                  bookImage.push(temp)
                  that.setData({
                    bookimages: bookImage
                  })
                }
              })
          }
        }
      }
    })
  },

  newbook: function(event){
    var bookId = event.currentTarget.dataset.bookId
    wx.navigateTo({
      url: '/pages/detail/detail?bookId=' + bookId,
    })
  }
})