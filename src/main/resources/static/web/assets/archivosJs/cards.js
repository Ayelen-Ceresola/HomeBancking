let { createApp } = Vue

createApp({
    data() {
        return {
            clients: [],
            cards: [],
            debitCard: [],
            creditCard: [],
            selectCard: "",
            selecteRenew:"",
        }
    },

    created() {
        this.loadData()
        this.getCards()

    },


    methods: {
        loadData() {
            axios.get("/api/clients/current")
                .then((res) => {
                    this.clients = res.data
                    console.log(this.clients)

                })
                .catch(err => console.log(err))
        },
        getCards() {
            axios.get("/api/clients/current/cards")
                .then((res) => {
                    this.cards = res.data
                    console.log(this.cards)
                    this.debitCard = this.cards.filter(card => card.type == "DEBIT").sort((a, b) => a.id - b.id)
                    console.log(this.debitCard)
                    this.creditCard = this.cards.filter(card => card.type == "CREDIT").sort((a, b) => a.id - b.id)
                    console.log(this.creditCard)

                })
                .catch(err => console.log(err))

        },
        selectColor(item) {
            let currentDay= new Date()
            if (item.color == "SILVER"){
                if (currentDay < new Date(item.thruDate)){
                    return "silverColor"
                }
                else return "expiredCard"
                
            }
            else if (item.color == "GOLD"){
                if(currentDay < new Date(item.thruDate)){
                    return "goldColor"
                }
                else return "expiredCard"
            }
            else if (item.color == "TITANIUM"){
                if(currentDay < new Date(item.thruDate)){
                    return "titaniumColor"
                }
                else return "expiredCard"
            }

        },
        dateCheck(thruDate){
            let currentDay= new Date()
            let formateThruDate = new Date(thruDate)
            return formateThruDate < currentDay;

        },
        renewCard(cardNumber){
            console.log(this.selecteRenew)
            this.selecteRenew = cardNumber
            axios.post(`/api/cards/renew?cardNumber=${this.selecteRenew}`)
                .then((res) => {
                    console.log(res)
                    window.location.reload()
                })
                .catch(err => console.log(err))

        },
        logout() {
            axios.post("/api/logout")
                .then((res) => {
                    window.location.href = "/web/pages/index.html"
                })
                .catch(err => console.log(err))
        },
        deletCard(cardNumber) {
            Swal.fire({
                title: 'Are you sure?',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                confirmButtonText: 'Yes',
                cancelButtonColor: '#d33',
                cancelButtonText: 'No',
                icon: 'question'


            }).then((res) => {
                if (res.isConfirmed) {

                    this.selectCard = cardNumber
                    axios.patch(`/api/clients/current/cards/delete?cardNumber=${this.selectCard}`)
                        .then((res) => {
                            Swal.fire({
                                position: 'center',
                                icon: 'success',
                                title: `Deleted card`,
                                showConfirmButton: false,
                                timer: 1500,
                            })
                            setTimeout(() => {
                                window.location.reload()
                            }, 2000)
                        })
                        .catch(err => {
                            Swal.fire({
                                position: 'center',
                                icon: 'error',
                                title: `error, try again`,
                                showConfirmButton: false,
                                timer: 1500,
                            })
                        })

                } else if (result.isDenied) {
                    Swal.fire("card not deletd", "", "info")
                }
            })
        }
    }

}).mount("#app")