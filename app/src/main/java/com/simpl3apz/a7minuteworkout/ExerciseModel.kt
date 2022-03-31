package com.simpl3apz.a7minuteworkout

class ExerciseModel (

    private var id : Int,
    private var name : String,
    private var image : Int,
    private var isCompleted : Boolean,
    private var isSelected : Boolean

        ){

    // getter and setter for Id
    fun getId() : Int {
        return this.id
    }
    fun setId( id : Int) {
        this.id = id
    }

    // getter and setter for name
    fun getName() : String {
        return this.name
    }
    fun setName( name : String) {
        this.name = name
    }

    // getter and setter for image
    fun getImage() : Int {
        return this.image
    }
    fun setImage( image : Int) {
        this.image = image
    }

    // getter and setter for isCompleted
    fun getIsCompleted() : Boolean {
        return this.isCompleted
    }
    fun setImage( isCompleted: Boolean) {
        this.isCompleted = isCompleted
    }

    // getter and setter for isSelected
    fun getIsSelected() : Boolean {
        return this.isSelected
    }
    fun setIsSelected( isSelected: Boolean) {
        this.isSelected = isSelected
    }

}