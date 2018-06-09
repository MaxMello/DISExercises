package de.dis2018.data

import de.dis2018.util.Helper

import javax.persistence.*

/**
 * Person-Bean
 */
@Entity
@Table(name = "person")
class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Int = 0

    @Column(name = "firstname")
    var firstname: String? = null

    @Column(name = "name")
    var name: String? = null

    @Column(name = "address")
    var address: String? = null

    @OneToMany(cascade = arrayOf(CascadeType.ALL), mappedBy = "contractPartner")
    private val contracts: Set<Contract>? = null

    override fun hashCode(): Int {
        val prime = 31
        var result = 1

        result = prime * result + if (firstname == null) 0 else firstname!!.hashCode()
        result = prime * result + if (name == null) 0 else name!!.hashCode()
        result = prime * result + if (address == null) 0 else address!!.hashCode()

        return result
    }

    override fun equals(obj: Any?): Boolean {
        if (this === obj)
            return true

        if (obj == null || obj !is Person)
            return false

        val other = obj as Person?

        return if (other!!.id != id ||
                !Helper.compareObjects(this.firstname, other.firstname) ||
                !Helper.compareObjects(this.name, other.name) ||
                !Helper.compareObjects(this.address, other.address)) {
            false
        } else true

    }
}
