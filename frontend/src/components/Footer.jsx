export default function Footer() {
  const year = new Date().getFullYear();
  return (
    <footer className="bg-gray-100 text-center py-6 mt-12">
      <p className="text-sm text-gray-500">
        © {year} VideoHub. All rights reserved.
      </p>
    </footer>
  );
}
