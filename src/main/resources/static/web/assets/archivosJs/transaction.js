let { createApp } = Vue

createApp({
    data() {
        return {
            clients: [],
            account: [],
            balance: [],
            getId: "",
            transactions: [],
            object: {
                amount: null,
                numberSourceAccount: "",
                numberDestinationAccount: "",
                description: "",
            },
            SelectDestination: "",
            allAcount: [],
            error:"",

        }
    },

    created() {
        this.loadData(),
            this.getClient()

    },


    methods: {

        loadData() {


            this.getId = new URLSearchParams(location.search).get("id")
            axios.get(`http://localhost:8080/api/accounts/${this.getId}`)
                .then((res) => {
                    this.account = res.data
                    this.object.numberSourceAccount = this.account.number
                    console.log(this.object)
                    console.log(this.numberSourceAccount)
                    this.balance = this.account.balance
                    this.transactions = this.account.transactions
                    this.transactions.sort((itemA, itemB) => itemA.id - itemB.id)


                })
                .catch(err => console.log(err))
        },
        getClient() {
            axios.get("http://localhost:8080/api/clients/current")
                .then((res) => {

                    this.clients = res.data
                    this.allAcount = this.clients.accounts


                })
                .catch(err => console.log(err))

        },
        createTransaction() {
            Swal.fire({
                title: 'Are you sure to send this transaction?',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                confirmButtonText: 'Yes',
                cancelButtonColor: '#d33',
                cancelButtonText: 'No',
                icon: 'question'


            }).then((res) => {
                if (res.isConfirmed) {
                    Swal.fire(
                        'Comfirm!',
                        'Transaction completed',
                        'success'

                    )
                    axios.post("/api/transactions", this.object)
                        .then((res) => {
                            this.response = res.data
                            console.log(this.response)
                            if (res.status == 200)
                                Swal.fire({
                                    position: 'center',
                                    icon: 'success',
                                    title: `${this.response}`,
                                    showConfirmButton: false,
                                    timer: 3000,
                                })
                                setTimeout(()=>{
                                    window.location.href = "accounts.html"
                                  },1800)

                        })
                        .catch(err => {
                            this.error = err.response
                            console.log(err)
                            console.log(this.error)
                            if (this.error.status == 403) {
                                Swal.fire({
                                    position: 'center',
                                    icon: 'error',
                                    title: `${this.error.data}`,
                                    showConfirmButton: false,
                                    timer: 3000,

                                })

                            }

                        })

                }

            })


        },


        logout() {
            axios.post("/api/logout")
                .then((res) => {
                    window.location.href = "/web/pages/index.html"
                })
                .catch(err => console.log(err))
        }
    }

}).mount("#app")