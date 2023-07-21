let {createApp} = Vue

createApp({
    data(){
        return {
            allAccounts: [],
            selectedAccount:"",
            loanPay:"",
            accountToPay:"",

                 
        }
    },

    created(){
      this.getAccounts(),
      this.loadData()

    },


    methods:{
        loadData() {
            this.getId = new URLSearchParams(location.search).get("id")
            axios.get(`/api/clients/current`)
                .then((res) => {
                    this.client = res.data
                    this.allLoans = this.client.loans
                    this.loanPay = this.allLoans.find(loan => loan.id ==  this.getId)

                    console.log(this.loanPay)
                    


                })
                .catch(err => console.log(err))
            },

        getAccounts() {
            axios.get(`/api/clients/current/accounts`)
                .then(res => {
                    console.log(res);
                    this.allAccounts = res.data.sort((a, b) => a.id - b.id)
                })
                .catch(err => console.log(err))
            },

            loanPaymets(){
                accountToPay = this.selectedAccount
                axios.post(`/api/clientLoan/payments?loanId=${this.idPay}&account=${this.accountToPay}`)
                .then((res)=>{
                    Swal.fire({
                        position: 'center',
                        icon: 'success',
                        title: `Payment correct`,
                        showConfirmButton: false,
                        timer: 1500
                    })
                    setTimeout(() => {
                        window.location.href = '/web/pages/accounts.html'
                    }, 1900)
                }).catch(err => {
                    this.errMsg = err.response.data
                    Swal.fire({
                        position: 'center',
                        icon: 'error',
                        title: `${this.errMsg}`,
                        showConfirmButton: false,
                        timer: 1500
                    })

                })
            },


        logout(){
            axios.post("/api/logout")
            .then((res) => {
                window.location.href= "/web/pages/index.html"
            })
            .catch(err => console.log(err))
        }
    }

}).mount ("#app")