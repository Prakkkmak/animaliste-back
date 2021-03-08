package com.progreizh.animaliste.dtos

import org.springframework.web.multipart.MultipartFile

data class PictureDto(
    val name : String,
    val file : MultipartFile
)