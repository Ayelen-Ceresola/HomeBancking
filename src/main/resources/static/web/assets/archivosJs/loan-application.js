let { createApp } = Vue

createApp({
    data() {
        return {
            clients: [],
            allAcount: [],
            loans: [],
            loanType: null,
            paymentsLoan: [],
            selectedLoan: "",

            amount: null,
            loanTypeId: null,
            payments: null,
            destinationAccount: "",

        }
    },

    created() {
        this.loadData(),
            this.getClient()


    },


    methods: {

        loadData() {
            axios.get("http://localhost:8080/api/loans")
                .then((res) => {
                    this.loans = res.data
                    console.log(this.loans)
                    this.loanType = this.loans.filter(loan => loan.id == this.selectedLoan)
                    console.log(this.loanType)
                    this.paymentsLoan = this.loanType[0].payments
                    console.log(this.paymentsLoan)


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
        getLoan() {
            console.log(this.amount, this.loanType, this.payments, this.destinationAccount)
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
                    Swal.fire(
                        'Comfirm!',
                        'Transaction completed',
                        'success'

                    )
                    axios.post("http://localhost:8080/api/loans", {

                        amount: this.amount,
                        loanTypeId: this.selectedLoan,
                        payments: this.payments,
                        destinationAccount: this.destinationAccount
                    })
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
                            setTimeout(() => {
                                window.location.href = "accounts.html"
                            }, 1800)

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
    },


}).mount("#app")