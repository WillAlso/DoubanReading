Page({
  data: {
    imageList1: [
      {
        imageUrl: '',
        bookId: '2567698'
      },
      {
        imageUrl: '',
        bookId: '10554308'
      },
      {
        imageUrl: '',
        bookId: '4913064'
      }
    ],
    imageList2: [
      {
        imageUrl: '',
        bookId: '34432189'
      },
      {
        imageUrl: '',
        bookId: '33416888'
      },
      {
        imageUrl: '',
        bookId: '33424433'
      }
    ],
    refreshflag: false,
    input: ""
  },
  searchBtn: function () {
    var con = this.data.input
    wx.navigateTo({
      url: '/pages/search/search?input=' + con,
    })
  },
  imageBtn: function(event){
    var bookId = event.currentTarget.dataset.bookId
    wx.navigateTo({
      url: '/pages/detail/detail?bookId=' + bookId,
    })
  },
  onLoad: function(){
    var that = this
    var cnt = 0
    for(var i = 0;i < 3;i++){
      var temp = that.data.imageList1[i].bookId
      wx.request({
        url: 'https://www.fenggangguo.xyz:81/book/' + temp + '?apikey=0df993c66c0c636e29ecbb5344252a4a',
        header: {
          "content-type": "json"
        },
        success: function(res){
          var p = 0
          if(res.data.id == that.data.imageList1[0].bookId){
            p = 0
          } else if (res.data.id == that.data.imageList1[1].bookId){
            p = 1;
          }else{
            p = 2;
          }
          var str = 'imageList1[' + p + '].imageUrl'
          that.data.refreshflag = true
          that.setData({
            [str]: res.data.image
          })
        }
      })
    }
    for (var i = 0; i < 3; i++) {
      var temp = that.data.imageList2[i].bookId
      wx.request({
        url: 'https://www.fenggangguo.xyz:81/book/' + temp + '?apikey=0df993c66c0c636e29ecbb5344252a4a',
        header: {
          "content-type": "json"
        },
        success: function (res) {
          var p = 0
          if (res.data.id == that.data.imageList2[0].bookId) {
            p = 0
          } else if (res.data.id == that.data.imageList2[1].bookId) {
            p = 1;
          } else {
            p = 2;
          }
          var str = 'imageList2[' + p + '].imageUrl'
          that.data.refreshflag = true
          that.setData({
            [str]: res.data.image
          })
        }
      })
    }
  },
  utilBtn: function(event){
    var barNum = event.currentTarget.dataset.barNum
    wx.navigateTo({
      url: '/pages/util/util?barNum=' + barNum,
    })
  },
  inputtext: function(res){
    this.setData({
      input: res.detail.value
    })
  }
})