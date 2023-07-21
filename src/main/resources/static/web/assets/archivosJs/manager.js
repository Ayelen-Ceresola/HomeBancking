let { createApp } = Vue

createApp({
    data() {
        return {
            obj: {
                firstName: "",
                lastName: "",
                email: "",
            },
            clients: [],
            json: "",

            amount: null,
            name: null,
            payments: [],
            percentage:null,

        }
    },

    created() {
        this.loadData()

    },

    methods: {
        loadData() {
            axios.get("/api/clients")
                .then((res) => {
                    this.json = res.data;
                    this.clients = res.data
                    
                })
                .catch(err => console.log(err))
        },
        postClient() {
            axios.post("/rest/clients", this.obj)
                .then((res) => {
                    this.loadData()
                })
                .catch(err => console.log(err))
        },
        addClient() {
            this.postClient()
        },
        addLoan() {
            console.log(this.name,this.amount,this.payments,this.percentage)
            let formatPayment = this.payments.split(`,`).map(Number)
            axios.post("/api/loans/create", {
                name: this.name,
                maxAmount: this.amount,
                payments: formatPayment,
                percentage: this.percentage,
            })
            .then((res) => {
                this.response = res.data
                console.log(this.response)
                if (res.status == 201)
                    Swal.fire({
                        position: 'center',
                        icon: 'success',
                        title: `${this.response}`,
                        showConfirmButton: false,
                        timer: 3000,
                    })
                setTimeout(() => {
                    window.location.reload()
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
    },
    logout() {
        axios.post("/api/logout")
            .then((res) => {
                window.location.href = "/web/pages/index.html"
            })
            .catch(err => console.log(err))
    }
}).mount("#app")