let { createApp } = Vue

createApp({
    data() {
        return {
            clients: [],
            account1: [],
            loans: [],
            accountsIsActive: [],
            selecteType:"",
            selectAccount: "",


        }
    },

    created() {
        this.loadData(),
        this.getAccount()
    },


    methods: {
        loadData() {
            axios.get("/api/clients/current")
                .then((res) => {

                    this.clients = res.data
                    this.account1 = res.data.accounts
                    this.account1.sort((a, b) => a.id - b.id)
                    this.loans = res.data.loans
                    this.loans.sort((a,b)=> a.id - b.id)

                })
                .catch(err => console.log(err))
        },
        logout() {
            axios.post("/api/logout")
                .then((res) => {
                    window.location.href = "/web/pages/index.html"
                })
                .catch(err => console.log(err.data.error))
        },
        createAccount() {
            Swal.fire({
              title: 'Select account type',
              input: 'select',
              inputOptions: {
                'SAVINGS': 'SAVINGS',
                'CURRENT': 'CURRENT'
              },
              showCancelButton: true,
              confirmButtonText: 'Create',
              cancelButtonText: 'Cancel',
              icon: 'question'
            }).then((result) => {
              if (result.isConfirmed) {
                const accountType = result.value;
                axios.post(`/api/clients/current/accounts?accountType=${accountType}`)
                  .then(res => {
                    console.log(res);
                    Swal.fire({
                      position: 'center',
                      title: 'Account created!',
                      showConfirmButton: false,
                      timer: 1500
                    })
                    setTimeout(()=>{
                        window.location.href = "accounts.html"
                      },1800)
                  })
                  .catch(err => {
                    console.log(err);
                    Swal.fire('Error creating account', '', 'error');
                  });
              }
            });
          
        },
        getAccount() {
            axios.get("/api/clients/current/accounts")
                .then((res) => {
                    console.log(res)
                    this.accountsIsActive = res.data
                    this.accountsIsActive.sort((a, b) => a.id - b.id)
                
                })
                .catch(err => console.log(err))

        },
        deleteAccount(number) {
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

                    this.selectAccount = number
                    axios.patch(`/api/clients/current/accounts/delete?number=${this.selectAccount}`)
                        .then((res) => {
                            Swal.fire({
                                position: 'center',
                                icon: 'success',
                                title: `Deleted account`,
                                showConfirmButton: false,
                                timer: 1500,
                            })
                            setTimeout(() => {
                                window.location.reload()
                            }, 2000)
                        })
                        .catch(err => {
                            console.log(err)
                            Swal.fire({
                                position: 'center',
                                icon: 'error',
                                title: `error, try again`,
                                showConfirmButton: false,
                                timer: 1500,
                            })
                        })

                } else if (result.isDenied) {
                    Swal.fire("account not deletd", "", "info")
                }
            })
        }
    }

}).mount("#app")