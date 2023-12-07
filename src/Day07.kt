enum class PairType : Comparable<PairType> {
    FiveOfAKind, FourOfAKind, FullHouse, ThreeOfAKind, TwoPair, OnePair, HighCard
}

private val listOfCards = listOf(
    'A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J'
)

private val listOfCards2 = listOf(
    'A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J'
)

open class CardPair(val cards: CharArray, val bid: Long) : Comparable<CardPair> {
    open fun getCardType(): PairType {
        val cardMap = cards.fold(mutableMapOf<Char, Int>()) { acc, c ->
            acc[c] = acc.getOrDefault(c, 0) + 1
            acc
        }

        return getPairTypeFromMap(cardMap)
    }

    protected fun getPairTypeFromMap(cardMap: MutableMap<Char, Int>): PairType {
        return when (cardMap.size) {
            1 -> PairType.FiveOfAKind
            2 -> {
                if (cardMap.any { it.value == 4 }) {
                    PairType.FourOfAKind
                } else {
                    PairType.FullHouse
                }
            }

            3 -> {
                if (cardMap.any { it.value == 3 }) {
                    PairType.ThreeOfAKind
                } else {
                    PairType.TwoPair
                }
            }

            4 -> {
                PairType.OnePair
            }

            else -> PairType.HighCard
        }
    }

    val type: PairType by lazy {
        getCardType()
    }

    protected fun compareWith(other: CardPair, cardList: List<Char>): Int {
        var comp = this.type.compareTo(other.type)
        return if (comp != 0) {
            comp
        } else {
            for (i in 0 until 5) {
                comp = cardList.indexOf(this.cards[i]).compareTo(cardList.indexOf(other.cards[i]))
                if (comp != 0) {
                    return comp
                }
            }
            0
        }
    }

    override fun compareTo(other: CardPair): Int = compareWith(other, listOfCards)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CardPair

        if (!cards.contentEquals(other.cards)) return false
        if (bid != other.bid) return false

        return true
    }

    override fun hashCode(): Int {
        var result = cards.contentHashCode()
        result = 31 * result + bid.hashCode()
        return result
    }

    override fun toString(): String {
        return "CardPair(cards=${cards.contentToString()}, bid=$bid, type=$type)"
    }
}

class CardPair2(cards: CharArray, bid: Long) : CardPair(cards, bid) {
    private val nbrJokers: Int = cards.count { it == 'J' }

    override fun getCardType(): PairType {
        val cardMap = cards.fold(mutableMapOf<Char, Int>()) { acc, c ->
            if (c != 'J') {
                acc[c] = acc.getOrDefault(c, 0) + 1
            }
            acc
        }

        if (cardMap.isNotEmpty()) {
            val maxi = cardMap.maxBy { it.value }.key
            cardMap[maxi] = cardMap[maxi]!! + nbrJokers
        } else {
            cardMap['J'] = 5
        }

        return getPairTypeFromMap(cardMap)
    }

    override fun compareTo(other: CardPair): Int = compareWith(other, listOfCards2)

    override fun toString(): String {
        return "CardPair(cards=${cards.contentToString()}, bid=$bid, type=$type, nbrJokers=$nbrJokers)"
    }
}

fun main() {
    fun <C : CardPair> Sequence<String>.process(mapper: (String) -> C): Long {
        return this.map {
            mapper.invoke(it)
        }.sortedDescending().fold(Triple<Long, Long, CardPair?>(0L, 1L, null)) { acc, cardPair ->
            val index = if (acc.third != null) {
                if (acc.third!!.compareTo(cardPair) != 0) {
                    acc.second + 1L
                } else {
                    acc.second
                }
            } else {
                1L
            }

            Triple(acc.first + index * cardPair.bid, index, cardPair)
        }.first
    }

    fun part1(input: List<String>): Long {
        return input.asSequence().process {
            val split = it.split(" ")
            CardPair(split[0].toCharArray(), split[1].toLong())
        }
    }

    fun part2(input: List<String>): Long {
        return input.asSequence().process {
            val split = it.split(" ")
            CardPair2(split[0].toCharArray(), split[1].toLong())
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 6440L)

    val input = readInput("Day07")
    part1(input).println()

    check(part2(testInput) == 5905L)

    part2(input).println()
}
