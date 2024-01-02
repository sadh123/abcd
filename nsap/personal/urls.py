# -*- coding: utf-8 -*-
"""
Created on Wed Jul 25 08:18:08 2018

@author: KHADKASADHANA
"""


from django.conf.urls import include,url
from . import views

urlpatterns = [
    url(r'^$',views.index,name='index'),
    url(r'^contact/',views.contact,name='contact'),

]
