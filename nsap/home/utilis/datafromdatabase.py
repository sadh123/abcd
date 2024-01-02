# -*- coding: utf-8 -*-
"""
Created on Sun May 20 14:47:35 2018

@author: KHADKASADHANA
"""

import psycopg2
def getfromdatabase(company):
    conn= psycopg2.connect(host="localhost",dbname="postgres",user="postgres",password="poojabhatta" )
    cur = conn.cursor()
    cur.execute("""select openprice,maxprice,minprice,closingprice,date from stockdata where  symbol = '%s' order by date;""" %company)
    row=cur.fetchall()
    conn.commit()
    cur.close()
    #print(row)
    return row

#getfromdatabase()