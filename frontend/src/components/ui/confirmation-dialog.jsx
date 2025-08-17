import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";

function ConfirmationDialog({
  isDialogOpen,
  onDialogStateChange,
  title = "Confirm Action",
  description = "Are you sure you want to proceed?",
  confirmText = "Confirm",
  cancelText = "Cancel",
  onConfirm,
  onCancel,
  variant = "default", // "default", "destructive"
}) {
  const handleConfirm = () => {
    onConfirm?.();
    onDialogStateChange(false);
  };

  const handleCancel = () => {
    onCancel?.();
    onDialogStateChange(false);
  };

  return (
    <Dialog open={isDialogOpen} onOpenChange={onDialogStateChange}>
      <DialogContent className="max-w-md">
        <DialogHeader>
          <DialogTitle className="text-xl font-semibold"> {title} </DialogTitle>
          <DialogDescription className="text-sm text-gray-600">
            {description}
          </DialogDescription>
        </DialogHeader>

        <DialogFooter className="gap-2">
          <Button variant="outline" onClick={handleCancel}>
            {cancelText}
          </Button>

          <Button
            variant={variant === "destructive" ? "destructive" : "default"}
            onClick={handleConfirm}
          >
            {confirmText}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}

export default ConfirmationDialog;
