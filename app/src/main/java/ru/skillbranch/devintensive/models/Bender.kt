package ru.skillbranch.devintensive.models

class Bender (var status: Status = Status.NORMAL, var question: Question = Question.NAME) {

    var wrongAnswerCounter = 0

    fun askQuestion(): String = when(question) {
        Question.NAME -> Question.NAME.question
        Question.PROFESSION -> Question.PROFESSION.question
        Question.MATERIAL -> Question.MATERIAL.question
        Question.BDAY -> Question.BDAY.question
        Question.SERIAL -> Question.SERIAL.question
        Question.IDLE -> Question.IDLE.question
    }

    fun listenAnswer(answer: String): Pair<String, Triple<Int, Int, Int>> {


        if(answerValidation(answer)){


        return if(wrongAnswerCounter < 3){

            if(question.answer.contains(answer)){
                wrongAnswerCounter = 0
                question = question.nextQuestion()
                "Отлично - ты справился\n${question.question}" to status.color
            } else{
                wrongAnswerCounter+=1
                status = status.nextStatus()
                "Это неправильный ответ\n${question.question}" to status.color
            }
        }else {
            wrongAnswerCounter = 0
            status = Status.values()[0]
            question = Question.values()[0]
            "Это неправильный ответ. Давай все по новой\n${question.question}" to status.color
        }
        }else {
            return "${question.validation}\n${question.question}" to status.color
        }
    }

    private fun answerValidation(answer: String): Boolean {
        return when(question){
            Question.NAME -> question.validateAnswer(answer)
            Question.PROFESSION -> question.validateAnswer(answer)
            Question.MATERIAL -> question.validateAnswer(answer)
            Question.BDAY -> question.validateAnswer(answer)
            Question.SERIAL -> question.validateAnswer(answer)
            Question.IDLE -> question.validateAnswer(answer)
        }
    }

    enum class Status(var color: Triple<Int, Int, Int>) {
        NORMAL(Triple(255, 255, 255)),
        WARNING(Triple(255, 120, 0)),
        DANGER(Triple(255, 60, 60)),
        CRITICAL(Triple(255, 255, 0));

        fun nextStatus(): Status {
            return if (this.ordinal < values().lastIndex)
                values()[this.ordinal + 1]
                else
                values()[0]
        }
    }

    enum class Question(var question: String, var answer: List<String>, var validation: String) {
        NAME("Как меня зовут?", listOf("Бендер", "Bender"), "") {
            override fun nextQuestion(): Question = PROFESSION
            override fun validateAnswer(answer: String): Boolean {
                return if (answer.first().isUpperCase())
                    true
                else {
                    validation = "Имя должно начинаться с заглавной буквы"
                    false
                }
            }
        },
        PROFESSION("Назови мою профессию?", listOf("сгибальщик", "bender"), "") {
            override fun nextQuestion(): Question = MATERIAL
            override fun validateAnswer(answer: String): Boolean {
                return if(answer.first().isLowerCase())
                    true
                else{
                    validation = "Профессия должна начинаться со строчной буквы"
                    false
                }
            }
        },
        MATERIAL("Из чего я сделан?", listOf("метал", "дерево", "metal", "iron", "wood"), "") {
            override fun nextQuestion(): Question = BDAY
            override fun validateAnswer(answer: String): Boolean {
                answer.forEach {
                    if(it.isDigit()) {
                        validation = "Материал не должен содержать цифр"
                        return false
                    }
                }
                return true
            }
        },
        BDAY("Когда меня создали?", listOf("2993"), "") {
            override fun nextQuestion(): Question = SERIAL
            override fun validateAnswer(answer: String): Boolean {
                answer.forEach {
                    if(it.isLetter()){
                        validation = "Год моего рождения должен содержать только цифры"
                        return false
                    }
                }
                return true
            }
        },
        SERIAL("Мой серийный номер?", listOf("2716057"), "") {
            override fun nextQuestion(): Question = IDLE
            override fun validateAnswer(answer: String): Boolean {
                if (iterateString(answer)) {
                    validation = "Серийный номер содержит только цифры, и их 7"
                    return false
                }
                return true
            }
            private fun iterateString(answer: String): Boolean {
                val len: Int = answer.map {
                    if(it.isLetter()) return true
                }.size
                return len < 7
            }
        },
        IDLE("На этом все, вопросов больше нет", listOf(), "") {
            override fun nextQuestion(): Question = IDLE
            override fun validateAnswer(answer: String): Boolean = true
        };

        abstract fun nextQuestion(): Question
        abstract fun validateAnswer(answer: String): Boolean
    }
}