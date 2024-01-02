# -*- coding: utf-8 -*-
"""
Created on Fri Jul 27 17:49:50 2018

@author: KHADKASADHANA
"""
import psycopg2


def connectDatabase():
    conn= psycopg2.connect(host="localhost",dbname="postgres",user="postgres",password="poojabhatta" ) 
    return conn

def getCur(conn):
    return conn.cursor()
