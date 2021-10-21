import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '@app/core/service/user';

@Component({
    selector: 'ac-account',
    templateUrl: './account.component.html',
})
export class AccountComponent implements OnInit {
    error: string | null;
    success: string | null;
    isSaving: Boolean;
    account: any;

    constructor(private userService: UserService, private route: ActivatedRoute, private router: Router) {}

    ngOnInit() {
        this.isSaving = false;
        this.userService
            .getLogueado()
            .toPromise()
            .then((account) => {
                this.account = account;
            });
    }

    save() {
        this.isSaving = true;
        this.userService.update(this.account).subscribe(
            () => {
                this.error = null;
                this.success = 'OK';
                this.isSaving = false;
                this.router.navigate(['account']);
            },
            () => {
                this.success = null;
                this.error = 'ERROR';
                this.isSaving = false;
            }
        );
    }

    clear() {
        // const with arrays: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Statements/const
        const returnPath = ['account'];
        this.router.navigate(returnPath);
    }

    isEditMode(): Boolean {
        if (this.route.snapshot.url.length == 0) {
            return false;
        }
        const lastPath = this.route.snapshot.url[this.route.snapshot.url.length - 1].path;
        return lastPath === 'edit';
    }
}
