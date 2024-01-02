# -*- coding: utf-8 -*-
"""
Created on Wed Jul 25 08:05:33 2018

@author: KHADKASADHANA
"""

from django.conf.urls import url
from . import views

urlpatterns = [
url(r'^$', views.index,name='index')
   
        ]