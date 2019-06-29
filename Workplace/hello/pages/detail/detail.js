Page({
  data: {

  },
  onLoad: function(options){
    console.log(options.id)
    this.setData({
      mid: options.id
  })
  var that = this
    wx.request({
      url: 'https://www.fenggangguo.xyz:81/movie/subject/' + options.id + '?apikey=0df993c66c0c636e29ecbb5344252a4a',
      header: {
        "content-type": "json"
      },
      success: function(res){
        if(res.statusCode == 200){
          that.setData({
            movie: res.data
          })
          wx.setNavigationBarTitle({
            title: res.data.rating.average + "分： " + res.data.title,
          })
          wx.hideNavigationBarLoading()
        }
      },
      fail: function(){
      },
      complete: function() {
      }
    })
  },
  onShareAppMessage: function(){
    return {
      title: "向你推荐： " + this.data.movie.title
    }
  }
})