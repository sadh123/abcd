# -*- coding: utf-8 -*-
"""
Created on Tue Jul 24 14:42:17 2018

@author: KHADKASADHANA
"""

from django.conf.urls import url
from home import views

urlpatterns = [
    url(r'^$',views.index,name='index'),
    url(r'^predictions/', views.predictions,name='predictions'),
    url(r'^result', views.result,name='result'),
 
]
