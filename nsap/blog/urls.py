# -*- coding: utf-8 -*-
"""
Created on Wed Jul 25 10:44:02 2018

@author: KHADKASADHANA
"""

from django.conf.urls import url, include
from django.views.generic import ListView, DetailView
from blog.models import Post

urlpatterns = [ 
                url(r'^$', ListView.as_view(
                                    queryset=Post.objects.all().order_by("-date")[:25],
                                    template_name="blog/blog.html")),
               url(r'^(?P<pk>\d+)$', DetailView.as_view(
                                    model = Post,
                                    template_name="blog/post.html")),
      
            ]