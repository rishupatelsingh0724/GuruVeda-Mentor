package com.example.guruvedamentor.Interface

import com.example.guruvedamentor.DataModel.PdfModel

interface PDFUpdateAndDelete {
    fun onUpdateClicked(position: Int, pdfModel: PdfModel)
    fun onDeleteClicked(position: Int, pdfModel: PdfModel)

}