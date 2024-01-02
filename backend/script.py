# -*- coding: utf-8 -*-
"""
Created on Fri Jul 27 16:42:13 2018

@author: KHADKASADHANA
"""
#import engine
from engine import prediction

company_list=['ADBL','CHCL','NABIL','NLIC','NTC','OHL','PLIC','SBI','SCB','SHL']
for company in company_list:
    prediction.execute(company)

#