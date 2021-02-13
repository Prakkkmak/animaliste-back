package com.progreizh.animaliste.dtos

import org.springframework.web.multipart.MultipartFile

data class PictureStorageDto(
    val name : String,
    val file : MultipartFile
)