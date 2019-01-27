package model

/**
 *
 * @autor Toropin Konstantin (impy.bian@gmail.com)
 */
data class Action(val id: Int, val name: String, val done: Boolean, val todoListId: Int)

data class TodoList(val id: Int, val name: String, val actions: List<Action>)