from urllib import request

url_request = request.Re
response = request.urlopen('https://book.douban.com/top250')
print(response.read())
